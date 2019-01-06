package com.anywhere.adelivery.utils

import android.os.Parcel
import android.os.Parcelable
import com.rd.draw.data.RtlMode
import com.rd.animation.type.AnimationType
import com.rd.draw.data.Orientation


class Customization : Parcelable {

    var animationType: AnimationType? = AnimationType.SLIDE
    var orientation = Orientation.HORIZONTAL
    var rtlMode: RtlMode? = RtlMode.Off

    var isInteractiveAnimation = false
    var isAutoVisibility = true
    var isFadeOnIdle = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as Customization?

        if (isInteractiveAnimation != that!!.isInteractiveAnimation) return false
        if (isAutoVisibility != that.isAutoVisibility) return false
        if (animationType != that.animationType) return false
        if (orientation !== that.orientation) return false
        return if (isFadeOnIdle != that.isFadeOnIdle) false else rtlMode == that.rtlMode

    }

    override fun hashCode(): Int {
        var result = if (animationType != null) animationType!!.hashCode() else 0
        result = 31 * result + orientation.hashCode()
        result = 31 * result + if (rtlMode != null) rtlMode!!.hashCode() else 0
        result = 31 * result + if (isInteractiveAnimation) 1 else 0
        result = 31 * result + if (isAutoVisibility) 1 else 0
        result = 31 * result + if (isFadeOnIdle) 1 else 0
        return result
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(if (this.animationType == null) -1 else this.animationType!!.ordinal)
        dest.writeInt(this.orientation.ordinal)
        dest.writeInt(if (this.rtlMode == null) -1 else this.rtlMode!!.ordinal)
        dest.writeByte(if (this.isInteractiveAnimation) 1.toByte() else 0.toByte())
        dest.writeByte(if (this.isAutoVisibility) 1.toByte() else 0.toByte())
        dest.writeByte(if (this.isFadeOnIdle) 1.toByte() else 0.toByte())
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        val tmpAnimationType = `in`.readInt()
        this.animationType = if (tmpAnimationType == -1) null else AnimationType.values()[tmpAnimationType]
        val tmpOrientation = `in`.readInt()
//        this.orientation = if (tmpOrientation == -1) null else Orientation.values()[tmpOrientation]
        val tmpRtlMode = `in`.readInt()
        this.rtlMode = if (tmpRtlMode == -1) null else RtlMode.values()[tmpRtlMode]
        this.isInteractiveAnimation = `in`.readByte().toInt() != 0
        this.isAutoVisibility = `in`.readByte().toInt() != 0
        this.isFadeOnIdle = `in`.readByte().toInt() != 0
    }

    companion object CREATOR : Parcelable.Creator<Customization> {
        override fun createFromParcel(parcel: Parcel): Customization {
            return Customization(parcel)
        }

        override fun newArray(size: Int): Array<Customization?> {
            return arrayOfNulls(size)
        }
    }
}