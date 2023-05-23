package si.sintal.sintaltehnika.ui.main;

import android.media.Image;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServisniNalog {
    int id;
    String delovniNalog;
    String Naziv;
    String OE;
    String datumZacetek;
    String datumKonec;
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
    String kodaObjekta;
    String pripadnost;
    String pripadnostNaziv;
    String vodjaNaloga;
    String narocnikNaziv;
    String narocnikNaslov;
    String narocnikUlica;
    String narocnikKraj;
    String narocnikHisnaSt;
    String narocnikSektor;
    String sektroNaslov;
    String sektorPostnaSt;
    int garancija;
    double ureDelo;
    double urePrevoz;
    double stKm;
    int oznacen;
    String datumDodeltive;
    String datumIzvedbe;
    String opisOkvare;
    String opisPostopka;
    String datumPodpisa;
    byte[] podpis;

    public int getid() {
        return id;
    }
    public void setid(int newId) {
        this.id = newId;
    }

    public double getUreDelo() {
        return ureDelo;
    }
    public void setUreDelo(double newUreDelo){this.ureDelo = newUreDelo;}

    public double getUrePrevoz() {
        return urePrevoz;
    }
    public void setUrePrevoz(double newUrePrevoz){this.urePrevoz = newUrePrevoz;}

    public double getStKm() {
        return stKm;
    }
    public void setStKm(double newStKm){this.stKm = newStKm;}

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
        //return datumZacetek;
        String vrniStr = "";
        if (datumZacetek.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datumZacetek);
        }
        return vrniStr;
    }
    public void setDatumZacetek(String newDatumZacetek) {
        this.datumZacetek = newDatumZacetek;
    }

    public String getDatumKonec() {
        //return datumKonec;
        String vrniStr = "";
        if (datumKonec.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datumKonec);
        }
        return vrniStr;
    }
    public void setDatumKonec(String newDatumKonec) {
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
    public void setNarocnikNaslov(String newNarocnikNaslov) { this.narocnikNaslov = newNarocnikNaslov; }

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

    public void setNarocnikKraj(String newNarocnikKraj) {
        this.narocnikKraj = newNarocnikKraj;
    }
    public String getNarocnikKraj()
    {
        return narocnikKraj;
    }

    public void setNarocnikSektor(String newNarocnikSektor) { this.narocnikSektor = newNarocnikSektor; }
    public String getNarocnikSektor()  { return narocnikSektor; }

    public void setNarocnikUlica(String newNarocnikUlica) { this.narocnikUlica = newNarocnikUlica; }
    public String getNarocnikUlica()  { return narocnikUlica; }

    public void setNarocnikHisnaSt(String newNarocnikHisnaSt) { this.narocnikHisnaSt = newNarocnikHisnaSt; }
    public String getNarocnikHisnaSt()  { return narocnikHisnaSt; }

    public void setKodaObjekta(String newKodaObjekta) { this.kodaObjekta = newKodaObjekta; }
    public String getKodaObjekta()  { return kodaObjekta; }

    public void setPripadnostNaziv(String newPripadnostNaziv) { this.pripadnostNaziv = newPripadnostNaziv; }
    public String getPripadnostNaziv()  { return pripadnostNaziv; }

    public void setIzdajatelj(String newIzdajatelj) { this.izdajatelj = newIzdajatelj; }
    public String getIzdajatelj()  { return izdajatelj; }

    public void setStatus(String newStatus) { this.status = newStatus; }
    public String getStatus()  { return status; }

    public void setTipNarocila(int newTipNarocila) { this.tipNarocila = newTipNarocila; }
    public int getTipNarocila()  { return tipNarocila; }

    public void setTipVzdrzevanja(int newTipVzdrzevanja) { this.tipVzdrzevanja = newTipVzdrzevanja; }
    public int getTipVzdzevanja()  { return tipVzdrzevanja; }

    public void setSektroNaslov(String newSektorNaslov) { this.sektroNaslov = newSektorNaslov; }
    public String getSektroNaslov() {return sektroNaslov;}

    public void setSektorPostnaSt(String newSektorPostnaSt) { this.sektorPostnaSt = newSektorPostnaSt; }
    public String getSektorPostnaSt() {return sektorPostnaSt;}

    public void setPripadnost(String newPripadnost) {this.pripadnost = newPripadnost;}
    public String getPripadnost(){return pripadnost;}

    public void setGarancija(int newGarancija) {this.garancija = newGarancija;}
    public int getGarancija(){return garancija;}

    public String getDatumDodelitve() {
        String vrniStr = "";
        if (datumDodeltive.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datumDodeltive);
        }
        return vrniStr;
    }
    public void setDatumDodelitve(String newDatumDodelitve) {this.datumDodeltive = newDatumDodelitve;  }

    public String getDatumIzvedbe() {
        //return datumIzvedbe;
        String vrniStr = "";
        if (datumIzvedbe.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datumIzvedbe);
        }
        return vrniStr;
    }
    public void setDatumIzvedbe(String newDatumIzvedbe) {this.datumIzvedbe = newDatumIzvedbe;  }

    public String getOpisOkvare() {
        return opisOkvare;
    }
    public void setOpisOkvare(String newOpisOkvare) {this.opisOkvare = newOpisOkvare;  }

    public String getOpisPostopka() {
        return opisPostopka;
    }
    public void setOpisPostopka(String newOpisPostopka) {this.opisPostopka = newOpisPostopka;  }

    public String getDatumPodpisa() {
        //return datumPodpisa;
        String vrniStr = "";
        if (datumPodpisa.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datumPodpisa);
        }
        return vrniStr;
    }
    public void setDatumPodpisa(String newDatumPodpisa) {this.datumPodpisa = newDatumPodpisa;  }

    public byte[] getPodpis() {
        return podpis;
    }
    public void setPodpis(byte[] newPodpis) {this.podpis = newPodpis;  }

    public String vrniDatum(String datumDb)
    {
        String vrniStr = "";
        try {
            String string_format = datumDb;
            Date date_format = new SimpleDateFormat("yyyy-MM-dd").parse(string_format);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            vrniStr = dateFormat.format(date_format);
        }
        catch (Exception e)
        {
            vrniStr = "";
        }
        return vrniStr;
    }
}
