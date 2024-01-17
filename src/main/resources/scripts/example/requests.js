function escapeString(input) {
    return `"${input.replaceAll("\"", "\\\\\"")}"`
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
    let pyCode = {}
    if (!input.url) return "No url provided"
    pyCode.url = escapeString(input.url)

    if (input.headers) {
        pyCode.headers = JSON.stringify(input.headers, null, 4)
    }

    if (input.form) {
        pyCode.files = `{\n${transformForm(input).join(",\n")}\n}`
    }

    if (input.params) {
        pyCode.params = JSON.stringify(input.params, null, 4)
    }

    if (input.data) {
        if (input.headers && input.headers["content-type"]) {
            if (input.headers["content-type"].indexOf("x-www-form-urlencoded") !== -1) {
                let body = {}
                for (let item of input.data.split("&")) {
                    let kv = item.split("=")
                    body[kv[0]] = kv[1]
                }
                pyCode.data = JSON.stringify(body, null, 4)
            } else if (input.headers["content-type"].indexOf("json") !== -1) {
                pyCode.json = JSON.stringify(JSON.parse(input.data), null, 4);
            } else {
                pyCode.data = escapeString(input.data)
            }
        } else {
            pyCode.data = escapeString(input.data)
        }
    }

    let codeBlock = ""
    let params = ""

    for (let key in pyCode) {
        codeBlock += `${key} = ${pyCode[key]}\n`
        params += `${key} = ${key}, `
    }

    return `${codeBlock}with requests.${input.method.toLowerCase()}(${params}verify=False) as resp:
    resp.raise_for_status()
    print(resp.text)`
}
