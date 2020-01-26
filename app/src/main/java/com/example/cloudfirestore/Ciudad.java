package com.example.cloudfirestore;

import android.os.Parcel;
import android.os.Parcelable;

public class Ciudad implements Parcelable {
    public static final Parcelable.Creator<Ciudad> CREATOR = new Parcelable.Creator<Ciudad>() {
        @Override
        public Ciudad createFromParcel(Parcel source) {
            return new Ciudad(source);
        }

        @Override
        public Ciudad[] newArray(int size) {
            return new Ciudad[size];
        }
    };
    private String ciudad;
    private String pais;
    private String imagen;

    public Ciudad(String ciudad, String pais, String imagen) {
        this.ciudad = ciudad;
        this.pais = pais;
        this.imagen = imagen;
    }

    public Ciudad() {
    }

    protected Ciudad(Parcel in) {
        this.ciudad = in.readString();
        this.pais = in.readString();
        this.imagen = in.readString();
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ciudad);
        dest.writeString(this.pais);
        dest.writeString(this.imagen);
    }
}
