package com.vjh0107.barcode.framework.exceptions

class DataNotFoundException : RuntimeException {
    constructor() : super("데이터가 존재하지 않습니다.")
    constructor(key: String) : super("키 $key 에 맞는 데이터가 존재하지 않습니다.")
}