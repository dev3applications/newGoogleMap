package com.deerbrain.googlemapsbase.Parcel

data class ReportAllData (
    val data: String  // not really sure how to set this up.
)


//in swift this is a struct i know kotlin doesn't use structs.  I'm not sure if a regular class or a data class or what is best for this in kotlin

// this struct will be created from decoding a JSON File

//public struct ReportAllData: Codable {
//    enum CodingKeys: String, CodingKey { case rpp, count, results, status, query, page }
//    var rpp: Int?
//    var count: Int?
//    var results: [Results] = []
//    var status: String?
//    var query: String?
//    var page: Int?
//
//    public struct Results: Codable {
//        enum CodingKeys: String, CodingKey { case latitude, land_use_class, county_name, mail_address3, county_id, addr_street_type, usps_residential, story_height, census_zip, addr_number, sale_price, muni_name, owner_occupied, mail_address1, trans_date, geom_as_wkt, state_abbr, robust_id, elevation, parcel_id, acreage_deeded, rausa_id, muni_id, land_use_code, owner, physcity, school_dist_id, addr_street_name, acreage_calc, longitude, physzip }
//        var latitude: String?
//        var land_use_class: String?
//        var county_name: String?
//        var mail_address3: String?
//        var county_id: Int?
//        var addr_street_type: String?
//        var usps_residential: String?
//        var story_height: String?
//        var census_zip: String?
//        var addr_number: String?
//        var sale_price: String?
//        var muni_name: String?
//        var owner_occupied: Bool?
//        var mail_address1: String?
//        var trans_date: String?
//        var geom_as_wkt: String?
//        var state_abbr: String?
//        var robust_id: String?
//        var elevation: String?
//        var parcel_id: String?
//        var acreage_deeded: String?
//        var rausa_id: Int?
//        var muni_id: Int?
//        var land_use_code: String?
//        var owner: String?
//        var physcity: String?
//        var school_dist_id: Int?
//        var addr_street_name: String?
//        var acreage_calc: String?
//        var longitude: String?
//        var physzip: String?
//    }
//}
