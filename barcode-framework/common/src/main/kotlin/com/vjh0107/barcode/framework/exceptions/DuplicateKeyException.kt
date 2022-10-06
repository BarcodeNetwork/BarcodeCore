package com.vjh0107.barcode.framework.exceptions

class DuplicateKeyException : RuntimeException {
    constructor() : super("동일한 키가 이미 존재합니다.")
    constructor(key: String) : super("$key 라는 동일한 키가 이미 존재합니다.")
}