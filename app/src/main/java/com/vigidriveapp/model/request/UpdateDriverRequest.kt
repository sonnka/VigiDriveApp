package com.vigidriveapp.model.request


data class UpdateDriverRequest(

    var dateOfBirth: String,

    var phoneNumber: String,

    var firstName: String,

    var lastName: String,

    var avatar: String
)