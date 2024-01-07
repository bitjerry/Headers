var curlTestObj = {
    "method": "POST",
    "url": "https://mcs.gov.su/fuck",
    "params": {
        "param1": "value1",
        "param2": "value2"
    },
    "headers": {
        ":abc": "123",
        "content-type": "application/json; charset=UTF-8",
        "authority": "mcs.gov.su",
        "accept": "*/*",
        "cookie": "key=value; key1=value1",
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    },
    "form": [
        {
            "name": "k1",
            "value": "v1"
        },
        {
            "name": "file",
            "value": "sometext",
            "type": "text/plain",
            "filename": "test"
        },
        {
            "name": "photo",
            "value": "@photo.png",
            "type": "text/plain",
            "filename": "test",
        },
        {
            "name": "photo",
            "value": "@photo.png",
            "type": "text/plain"
        }
    ],
    "body": "[{\n" +
        "    \"error\": 0,\n" +
        "    \"status\": \"success\",\n" +
        "    \"date\": \"9999-12-31\"\n" +
        "}]"
}

