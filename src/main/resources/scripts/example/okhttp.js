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

function transformForm(input) {
    let formData = "RequestBody formData = new MultipartBody.Builder()\n.setType(MultipartBody.FORM)\n"
    input.form.forEach((item, index) => {
        let formParams = []
        formParams.push(`${escapeString(item.name)}`)
        if (item.value.startsWith("@")) {
            formParams.push(`${escapeString(item.filename ? item.filename : item.value.substring(1))}`)
            let fileParams = []
            if (item.type) {
                fileParams.push(`MediaType.parse("${item.type}")`)
            }
            fileParams.push(`new File(${escapeString(item.value)})`)
            formParams.push(`RequestBody.create(${fileParams.join(", ")})`)
        } else {
            formParams.push(`${escapeString(item.value)}`)
        }
        formData += `.addFormDataPart(${formParams.join(", ")})\n`
    })
    return formData + ".build();\n\n"
}

function transformParams(input) {
    let params = []
    for (let k in input.params) {
        params.push(`${k}=${input.params[k]}`)
    }
    return params.join("&")
}


function transform(input) {
    if (!input.url) return "No url provided"
    let javaCode = "OkHttpClient client = new OkHttpClient();\n\n"

    let bodyField = undefined
    if (!isEmpty(input.data)) {
        bodyField = "body"
        javaCode += `String body = ${escapeString(input.data)};\n\n`
    } else if (!isEmpty(input.form)) {
        bodyField = "formData"
        javaCode += transformForm(input)
    }

    javaCode += "Request request = new Request.Builder()\n"

    if (!isEmpty(input.params)) {
        input.url += "?" + transformParams(input);
    }
    javaCode += `.url("${input.url}")\n`

    if (bodyField) {
        javaCode += `.${input.method.toLowerCase()}(${bodyField})\n`
    } else {
        javaCode += `.${input.method.toLowerCase()}()\n`
    }

    if (!isEmpty(input.headers)) {
        for (let key in input.headers) {
            javaCode += `.addHeader("${key}", ${escapeString(input.headers[key])})\n`
        }
    }

    javaCode += ".build();"


    return `${javaCode}\n\ntry (Response response = client.newCall(request).execute()) {
    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
    System.out.println(response.body());
}
catch (Exception e){
    e.printStackTrace();
}`
}
