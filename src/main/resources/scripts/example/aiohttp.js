function escapeString(input) {
    return `"${input.replaceAll("\"", "\\\\\"")}"`
}

function transformForm(input) {

    let formData = "aiohttp.FormData(quote_fields=False)\n"
    input.form.forEach((item, index) => {
        let params = [];
        params.push(`name=${escapeString(item.name)}`)
        if (item.value.startsWith("@")) {
            params.push(`value=open(${escapeString(item.value)}, "rb").read()`)
        }
        else {
            params.push(`value=${escapeString(item.value)}`)
        }
        if (item.filename) {
            params.push(`filename=${escapeString(item.filename)}`)
        }
        if (item.type){
            params.push(`content_type=${escapeString(item.type)}`)
        }
        formData += `data.add_field(${params.join(",")})\n`

    })
    return formData
}

// function transformJSON(){
//     if (input.headers && input.headers["content-type"].indexOf("json") !== -1){
//         return JSON.stringify(input.data, null, 4);
//     }
// }

// function transformCookies(){
//     input
// }

function transform(input) {
    let pyCode = {}
    if (!input.url) return "No url provided"
    pyCode.url = escapeString(input.url)

    if (input.headers) {
        pyCode.headers = JSON.stringify(input.headers, null, 4)
    }

    if (input.form) {
        pyCode.data = transformForm(input)
    }

    if (input.params) {
        pyCode.params = JSON.stringify(input.params, null, 4)
    }

    if (input.body) {
        pyCode.data = escapeString(input.body)
    }

    let codeBlock = ""
    let params = ""

    for (let key in pyCode) {
        codeBlock += `${key} = ${pyCode[key]}\n`
        params += `${key} = ${key}, `
    }

    return `${codeBlock}async with aiohttp.ClientSession() as session:
    async with session.${input.method.toLowerCase()}(${params}ssl=False) as res:
        text = await res.text()`
}
