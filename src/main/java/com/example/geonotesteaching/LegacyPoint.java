package com.example.geonotesteaching;

import java.util.Objects;

/*
* Las ventajas de usar record son:
*       Genera automaticamente los getters y los setters.
*       Genera automaticamente el constructor.
*       Genera automaticamente lo equals(), toString() y hashCode.
*       Los campos son final por lo que no se cambiarán accidentalmente, esto sirve para tener un código más limpio.
*       Elimina código repetitivo de uso frecuente y facilita el mantenimiento y legibilidad.
*       Compatible con patrones sealed o pattern matching.
*
* ¿Cuando no usar record?
* Cuando el constructor es complejo y necesitas mutabilidad de los campos una vez creado el objeto.
* Cuando necesitas extender otras clases, ya que solo pueden implementar interfaces.
* Cuando no te conviene usar todos los campos en equals() y hasCode().
*/
public class LegacyPoint{
    private double lat,lon;

    public LegacyPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "LegacyPoint{} " + "latitud = " + lat + "longitud = " + lon;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        else if (o == null || getClass() != o.getClass()) return false;
        if (!(o instanceof LegacyPoint)) {
            return false;
        }else{
            return this == o;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
