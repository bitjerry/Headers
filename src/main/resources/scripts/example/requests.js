/**
 * @author Mr.lin
 * @version 1.0.2
 *
 */

function escapeString(input) {
    return `"${input.replaceAll("\"", "\\\\\"")}"`
}

function isEmpty(value) {
    return !value || typeof value == 'object' && Object.keys(value).length === 0
}

function jsonReplacer(key, value) {
    if (typeof value == "boolean") {
        return value ? "true" : "false"
    } else if (typeof value == "string") {
        return decodeURIComponent(value)
    } else {
        return value
    }
}

function transformForm(input) {
    let formData = []
    input.form.forEach((item, index) => {
        if (item.value.startsWith("@")) {
            if (!item.filename) {
                item.filename = item.value.substring(1)
            }
            item.value = `open(${escapeString(item.value)}, 'rb')`
            if (Object.keys(item).length === 2) {
                formData.push(`${escapeString(item.name)}: ${item.value}`)
                return
            }
        } else {
            item.value = escapeString(item.value)
        }
        let params = []
        if (item.filename) {
            params.push(escapeString(item.filename))
        } else {
            params.push("None")
        }

        params.push(item.value)
        if (item.type) {
            params.push(escapeString(item.type))
        }
        formData.push(`${escapeString(item.name)}: (${params.join(", ")})`)
    })
    return formData
}

function transform(input) {
    let pyCode = []
    let params = []
    if (!input.url) return "No url provided"
    pyCode.push(`url = ${escapeString(input.url)}`)
    params.push("url = url")

    if (!isEmpty(input.headers)) {
        pyCode.push(`headers = ${JSON.stringify(input.headers, null, 4)}`)
        params.push("headers = headers")
    }

    if (!isEmpty(input.form)) {
        pyCode.push("files = " + `{\n${transformForm(input).join(",\n")}\n}`)
        params.push("files = files")
    }

    if (!isEmpty(input.params)) {
        pyCode.push(`params = ${JSON.stringify(input.params, jsonReplacer, 4)}`)
        params.push("params = params")
    }

    if (!isEmpty(input.data)) {
        if (input.headers && input.headers["content-type"]) {
            if (input.headers["content-type"].indexOf("x-www-form-urlencoded") !== -1) {
                let body = {}
                for (let item of input.data.split("&")) {
                    let kv = item.split("=")
                    body[kv[0]] = jsonReplacer(undefined, kv[1])
                }
                pyCode.push(`data = ${JSON.stringify(body, null, 4)}`)
                params.push("data = data")
            } else if (input.headers["content-type"].indexOf("json") !== -1) {
                pyCode.push(`json_data = ${JSON.stringify(JSON.parse(input.data), jsonReplacer, 4)}`)
                params.push(`json = json_data`)
            } else {
                pyCode.push(`data = ${escapeString(input.data)}`)
                params.push("data = data")
            }
        } else {
            pyCode.push(`data = ${escapeString(input.data)}`)
            params.push("data = data")
        }
    }

    return `${pyCode.join("\n")}\n\nwith requests.${input.method.toLowerCase()}(${params.join(",")}, verify=False) as resp:
    resp.encoding = 'utf-8'
    resp.raise_for_status()
    print(resp.text)`
}
