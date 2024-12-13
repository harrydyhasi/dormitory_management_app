package com.example.dormitory_management.student.models
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class UserModel(
    val fullName: String?,
    val cccd: String?,
    val email: String?,
    val date: Date?,
    val gender: String?,
    val password:String?,
    val phoneNumber: String?,
    val idRoom: String?,
    val dateStart: Date?,
    val dateEnd: Date?,
    val dateReturn:Date?,
    val price: Long?,
    val status: String?,
    val statusReturn: String?,
    val role: String?
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("date"),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("dateStart"),
        TODO("dateEnd"),
        TODO("dateReturn"),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor() : this(
        "",
        "",
        "",
        null,
        "",
        "",
        "",
        "",
        null,
        null,
        null,
        0,
        "",
        "",
        "student"
    )

    constructor(dataSnapshot: DocumentSnapshot): this(
        dataSnapshot.getString("fullName"),
        dataSnapshot.getString("cccd"),
        dataSnapshot.getString("email"),
        dataSnapshot.getDate("date"),
        dataSnapshot.getString("gender"),
        "",
        dataSnapshot.getString("phoneNumber"),
        dataSnapshot.getString("idRoom"),
        dataSnapshot.getDate("dateStart"),
        dataSnapshot.getDate("dateEnd"),
        dataSnapshot.getDate("dateReturn"),
        dataSnapshot.getLong("price"),
        dataSnapshot.getString("status"),
        dataSnapshot.getString("statusReturn"),
        dataSnapshot.getString("role")
        )

    constructor(
        fullName: String,
        cccd: String,
        email: String,
        date: String,
        gender: String,
        password: String,
        phoneNumber: String,
        idRoom: String,
        dateStart: Nothing?,
        dateEnd: Nothing?,
        dateReturn: Nothing?,
        price: Long,
        status: String,
        statusReturn: String
    ) : this()

    override fun toString(): String {
        return "UserModel(fullName=$fullName, cccd=$cccd, email=$email, date=$date, gender=$gender, password=$password, phoneNumber=$phoneNumber, idRoom=$idRoom, dateStart=$dateStart, dateEnd=$dateEnd, dateReturn=$dateReturn, price=$price, status=$status, statusReturn=$statusReturn, role=$role)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeString(cccd)
        parcel.writeString(email)
        parcel.writeString(gender)
        parcel.writeString(password)
        parcel.writeString(phoneNumber)
        parcel.writeString(idRoom)
        parcel.writeValue(price)
        parcel.writeString(status)
        parcel.writeString(statusReturn)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}