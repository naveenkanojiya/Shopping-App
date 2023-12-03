package com.example.shoppingappadmin

class ProductModel {
    var name: String = ""
    var price: Double = -1.0
    var disp : String = ""
    var imageUrl : String = ""
    var category : String = ""

    constructor(name: String, price: Double, disp: String, imageUrl: String) {
        this.name = name
        this.price = price
        this.disp = disp
        this.imageUrl = imageUrl
    }


    constructor()
    constructor(name: String, price: Double, disp: String, imageUrl: String, category: String) {
        this.name = name
        this.price = price
        this.disp = disp
        this.imageUrl = imageUrl
        this.category = category
    }
}