package com.vjh0107.barcode.framework.exceptions

class KeyNotFoundException : RuntimeException {
    constructor() : super("키가 존재하지 않습니다.")
    constructor(key: String) : super("$key 라는 키가 존재하지 않습니다.")
}