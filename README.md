# Headers


---
[![Download](https://img.shields.io/jetbrains/plugin/d/18299?style=flat-square)](https://plugins.jetbrains.com/plugin/18299-headers)
[![Version](https://img.shields.io/jetbrains/plugin/v/18299?style=flat-square)](https://plugins.jetbrains.com/plugin/18299-headers/versions)
[![Star](https://img.shields.io/jetbrains/plugin/r/stars/18299?label=Headers&style=flat-square)](https://plugins.jetbrains.com/plugin/18299)
[![APM](https://img.shields.io/github/license/bitjerry/Headers?color=blue&style=flat-square)](./LICENSE)

This tool assists programmers in quickly handling browser request message information.

Read in other languages: English | [简体中文](./README.zh-CN.md)

## Installation

---
<a href="https://plugins.jetbrains.com/plugin/18299-headers" target="_blank">
    <img src="https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/installation_button.svg" height="52" alt="Get from Marketplace" title="Get from Marketplace">
</a>

<!-- Plugin description -->

## Features

---
1. Quickly format headers or cookies text copied from the browser into JSON format.
2. Generate request code based on curl commands copied from the browser.
3. Custom code generator.

## Usage

---

### Convert Request Headers

> Quickly convert request headers, cookies, and formData to JSON format.  
> Can be directly used as a Python dictionary.

1. Copy the message headers from the browser or select existing code, then right-click on "headers" or "cookies" in the code editing area.
2. The plugin automatically generates the corresponding content in JSON format and pastes it into the cursor position or replaces the current text.

![Header Conversion](https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/headers.gif)

### Generate Request Code

> Generate request code based on curl commands.  
> The plugin provides three default code generation scripts: aiohttp, request, okhttp.

1. Copy the curl command. When pasting in IntelliJ IDEA, right-click on "headers" in the context menu corresponding to the curl code generation script.
2. The plugin automatically inserts the code at the cursor position.

![Curl Command Generation](https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/curl.gif)

### Custom Code Generation Script

> Customize the curl request conversion script to generate any request code.

1. Use any editor to write a JavaScript script that must include a globally scoped `transform` function, taking a [curl object](https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/src/main/resources/scripts/test.js) as the only parameter and returning the generated code string.
2. Open Settings -> Tools -> Headers, add a script to the left script list, and paste the script created earlier into the right editor.
3. You can now right-click on "headers" in IDEA to see the corresponding code generation script for curl command transformation.

![Custom Script](https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/custom_script.png)

<!-- Plugin description end -->

## FAQ

1. Why can't my copied curl command be parsed or has missing parameters?
    - The plugin's curl command parser doesn't support all parameters. It mainly focuses on curl commands from the browser. If you have specific requirements, feel free to raise an issue for enhancement.
      Please select the bash format when copying curl commands from the browser.
2. Is the generated code incorrect?
    - The plugin's default scripts were tested with some examples from the curl documentation. However, due to the complexity of curl, there might be omissions or issues with generated code. You can modify the scripts to address any issues with the generated code.
3. Why is the generated code format messy?
    - Check if auto-formatting and maintaining indentation are enabled in the plugin options. If formatting fails, ensure that the corresponding language support plugin is installed in IDEA.
4. Is the script execution slow?
    - After each script modification, the initial execution might be slower, but subsequent executions will be faster due to the compilation cache.
5. Method not found, undefined, or syntax errors?
    - The scripts support some ES6 features. Please refer to [nashorn JavaScript support for ES6](https://developer.oracle.com/zh/learn/technical-articles/nashorn-javascript-part1).
6. Why JavaScript and nashorn?
    - JavaScript was chosen for code generation scripts due to its flexibility compared to template languages. For most users dealing with browser-requested data, JavaScript is more user-friendly.
      Nashorn, while somewhat dated and criticized, is lightweight and sufficient for code generation. If you have better suggestions, feel free to raise an issue.

### License

---
Apache 2.0 © [bitjerry](./LICENSE)
  
*2021/11/22*
*Mr.lin*
