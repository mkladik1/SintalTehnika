package si.sintal.sintaltehnika.ui.main;

import java.util.Date;

public class ServisniNalog {
    int id;
    String delovniNalog;
    String Naziv;
    String OE;
    String datumZacetek;
    Date datumKonec;
    String opis;
    String klient;
    String tipDN;
    String status;
    String vrstaDN;
    String objekt;
    String odgovornaOseba;
    String izdajatelj;
    int tipNarocila;
    int tipVzdrzevanja;
    String kodaObejkta;
    String pripadnost;
    String pripadnostNaziv;
    String vodjaNaloga;
    String narocnikNaziv;
    String narocnikNaslov;
    String narocnikPosta;
    String narocnikKraj;
    String narocnikSektor;
    String narocnikUlica;
    String narocnikHisnaSt;
    int oznacen;

    public int getid() {
        return id;
    }

    public void setid(int newId) {
        this.id = newId;
    }

    public String getDelovniNalog() {
        return delovniNalog;
    }

    public void setDelovniNalog(String newDelovniNalog) {
        this.delovniNalog = newDelovniNalog;
    }

    public String getDNaziv() {
        return Naziv;
    }

    public void setNaziv(String newNaziv) {
        this.Naziv = newNaziv;
    }

    public String getOE() {
        return OE;
    }

    public void setOE(String newOE) {
        this.OE = newOE;
    }

    public String getDatumZacetek() {
        return datumZacetek;
    }

    public void setDatumZacetek(String newDatumZacetek) {
        this.datumZacetek = newDatumZacetek;
    }

    public Date getDatumKonec() {
        return datumKonec;
    }

    public void setDatumKonec(Date newDatumKonec) {
        this.datumKonec = newDatumKonec;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String newOpis) {
        this.opis = newOpis;
    }

    public String getNarocnikNaziv() {
        return narocnikNaziv;
    }

    public void setNarocnikNaziv(String newNarocnikNaziv) {
        this.narocnikNaziv = newNarocnikNaziv;
    }

    public String getVodjaNaloga() {
        return vodjaNaloga;
    }

    public void setVodjaNaloga(String newVodjaNaloga) {
        this.vodjaNaloga = newVodjaNaloga;
    }

    public String getNarocnikNaslov() {
        return narocnikNaslov;
    }

    public void setNarocnikNaslov(String newNarocnikNaslov) {
        this.narocnikNaslov = newNarocnikNaslov;
    }

    public String getOdgovornaOseba() {
        return odgovornaOseba;
    }

    public void setOdgovornaOseba(String newOdgOseba) {
        this.odgovornaOseba = newOdgOseba;
    }

    public int getOznacen() {
        return oznacen;
    }

    public void setOznacen(int newOznacen) {
        this.oznacen = newOznacen;
    }

}
