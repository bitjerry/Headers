# Headers

---
[![Download](https://img.shields.io/jetbrains/plugin/d/18299?style=flat-square)](https://plugins.jetbrains.com/plugin/18299-headers)
[![Version](https://img.shields.io/jetbrains/plugin/v/18299?style=flat-square)](https://plugins.jetbrains.com/plugin/18299-headers/versions)
[![Star](https://img.shields.io/jetbrains/plugin/r/stars/18299?label=Headers&style=flat-square)](https://plugins.jetbrains.com/plugin/18299)
[![APM](https://img.shields.io/github/license/bitjerry/Headers?color=blue&style=flat-square)](./LICENSE)
  
此工具帮助程序员快速处理浏览器请求报文信息  
  
使用其他语言阅读：[English](./README.md) | 简体中文

## 警告

这是一个由 IDEA 内嵌 rhino 引擎构建的版本.  
由于 rhino 版本较低, 对JS支持有许多问题, 如果运行时报错不用担心, 它是很正常的.  
该分支仅用于测试, 不会发布.

## 安装

---
<a href="https://plugins.jetbrains.com/plugin/18299-headers" target="_blank">
    <img src="https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/installation_button.svg" height="52" alt="Get from Marketplace" title="Get from Marketplace">
</a>

## 功能

---
1. 帮助你快速将从浏览器复制出来的headers或者cookies文本快速格式化为JSON格式
2. 根据从浏览器复制出来的curl命令生成请求代码
3. 自定义代码生成器

## 用法

---

### 转换请求头

> 将请求报文头, cookies, formData快速转为JSON格式  
> 可直接作为 python 字典

1. 复制浏览器中的报文头或选择已有代码, 在代码编辑区右键点击 headers 或者 cookies
2. 插件自动以 JSON 格式生成相应内容, 并粘贴到光标处或替换当前文本

<p align="center"><img src="https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/headers.gif" alt="screenshots"></p>

### 生成请求代码

> 根据 curl 命令生成请求代码
> 插件提供三个默认代码生成脚本, 分别为 aiohttp, request, okhttp. 

1. 复制 curl 命令, 当你在 IDEA 中粘贴时, 右键菜单 headers 选择 curl 对应代码生成脚本
2. 插件自动将代码插入光标对应位置

<p align="center"><img src="https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/curl.gif" alt="screenshots"></p>

### 自定义代码生成脚本

> 自定义 curl 请求转换脚本, 生成任意请求代码

1. 使用任意编辑器编写一个 JavaScript 脚本, 必须包括一个全局作用域的 `transform` 函数, 接受一个 [curl 对象](https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/src/main/resources/scripts/test.js) 作为唯一参数, 返回生成的代码字符串
2. 打开设置 -> 工具 -> headers, 左侧脚本列表添加一个脚本, 在右侧编辑器中粘贴前方写好的脚本
3. 即可在 IDEA 中右键 headers -> curl 看到对应的代码生成脚本, 转换 curl 命令

<p align="center"><img src="https://cdn.jsdelivr.net/gh/bitjerry/Headers@main/images/custom_script.png" alt="screenshots"></p>

## 常见问题

1. 为什么我复制的 curl 命令无法解析或有遗漏参数?  
    答: 插件实现的 curl 命令解析器并不支持所有参数, 该插件重点关注浏览器中的 curl 命令. 
    当然, 如果你有需求欢迎提 issue, 我很乐意完善这个解析器.  
    在浏览器中选择 curl 命令时, 请选择 bash 格式.
2. 生成的代码有误?  
    答: 插件自带的三个脚本通过参照 curl 文档的部分例子进行测试. curl 是一个复杂的工具, 难免有遗漏. 
    此外, 因为库版本原因, 生成的代码可能包含无效的 API. 对于生成代码问题你只需要修改脚本即可.
3. 为什么生成的代码格式混乱?
    答: 请查看插件选项中是否勾选了自动格式化和保持缩进. 如果格式化失败, 请检查 IDEA 是否安装了对应语言的支持插件.
4. 脚本执行慢?  
    答: 脚本每次修改后执行可能会慢些, 但之后会从编译缓存中执行脚本. 
5. 未找到xxx方法, undefined, 语法错误?  
    答: 脚本支持 ES6 部分标准, 请参考 [nashorn JavaScript 对 ES6 的支持](https://developer.oracle.com/zh/learn/technical-articles/nashorn-javascript-part1)
6. 为什么是 JavaScript 和 nashorn?  
    答: 选择 JavaScript 作为代码生成脚本, 一是考虑到其相较于模板语言的灵活性, 二是对于大部分需要从浏览器扣请求的用户而言, JavaScript 更易上手.
    nashorn 确实比较古老, 也有许多令人诟病的地方, 但它足够轻量, 对于代码生成而言完全够用. 如果你有更好的建议欢迎提 issue.


## 许可

---
Apache 2.0 © [bitjerry](./LICENSE)
  
*2021/11/22*
*Mr.lin*
