package com.vigidriveapp.model.request


data class UpdateDriverRequest(

    var avatar: String,

    var firstName: String,

    var lastName: String,

    var dateOfBirth: String,

    var phoneNumber: String
)