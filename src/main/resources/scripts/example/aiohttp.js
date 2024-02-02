/**
 * @author Mr.lin
 * @version 1.0.0
 *
 */

function escapeString(input) {
    return `"${input.replaceAll("\"", "\\\\\"")}"`
}

function isEmpty(value) {
    return !value || typeof value == 'object' && Object.keys(value).length === 0
}

function jsonReplacer(key, value) {
    if (typeof value == "boolean"){
        return value ?  "true" : "false"
    }
    else if (typeof value == "string"){
        return decodeURI(value)
    }
    else {
        return value
    }
}

function transformForm(input) {

    let formData = "aiohttp.FormData(quote_fields=False)\n"
    input.form.forEach((item, index) => {
        let params = [];
        params.push(`name=${escapeString(item.name)}`)
        if (item.value.startsWith("@")) {
            params.push(`value=open(${escapeString(item.value)}, "rb")`)
        } else {
            params.push(`value=${escapeString(item.value)}`)
        }
        if (item.filename) {
            params.push(`filename=${escapeString(item.filename)}`)
        }
        if (item.type) {
            params.push(`content_type=${escapeString(item.type)}`)
        }
        formData += `form_data.add_field(${params.join(",")})\n`

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

    if (!isEmpty(input.params)) {
        pyCode.push(`params = ${JSON.stringify(input.params, jsonReplacer, 4)}`)
        params.push("params = params")
    }

    if (!isEmpty(input.form)) {
        pyCode.push(`form_data = ${transformForm(input)}`)
        params.push("data = form_data")
    } else if (!isEmpty(input.data)) {
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
                pyCode.push(`json = ${JSON.stringify(JSON.parse(input.data), jsonReplacer, 4)}`)
                params.push(`json = json`)
            } else {
                pyCode.push(`data = ${escapeString(input.data)}`)
                params.push("data = data")
            }
        } else {
            pyCode.push(`data = ${escapeString(input.data)}`)
            params.push("data = data")
        }

    }

    return `${pyCode.join("\n")}\n\nasync with aiohttp.ClientSession() as session:
    async with session.${input.method.toLowerCase()}(${params.join(",")}, ssl=False) as res:
        print(await res.text())`
}

