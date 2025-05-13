package si.sintal.sintaltehnika.ui.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DelovniNalogVZ {

    int id;
    int sintal_vzd_dn_id;
    String st_del_naloga;
    String naziv_servisa;
    String r_objekt_id;
    String ime;
    String mc_objekt_fakt;
    String mc_klient;
    String mc_ime_objekta;
    String pogodba;
    String periodika_dni;
    String kontaktna_oseba;
    String opomba;
    String telefon;
    String datum_zadnjega;
    String datum_naslednjega;
    int periodikaDni;

    int sis_pozar;
    int sis_vlom;
    int sis_video;
    int sis_co;
    int sis_pristopna;
    int sis_dimni_bankovci;
    int sis_ostalo;
    String oprema;
    String prenos_alarma;
    int Dok_BD;
    String datum_veljavnosti_Dok_BD;
    int serviser_izvajalec;
    String koda_obejkta;
    byte[] podpis_vzdrzevalec;
    byte[] podpis_narocnik;
    int status;
    String DATUM_DODELITVE;
    String DATUM_IZVEDBE;
    int prenos;
    String DATUM_PRENOSA;
    String OPIS_OKVARE;
    String OPIS_POSTOPKA;
    double ureDelo;
    double urePrevoz;
    double stKm;
    String datumPodpisa;
    String naslov;
    int periodika_kreirana;
    int prenos_per;
    String narocnik;
    String narocnik_naslov;
    String objekt;
    String objekt_naslov;
    String leto_mes_obr;



    public int getid() {
        return id;
    }
    public void setid(int newId) {
        this.id = newId;
    }

    public String getLetoMesObr() {
        return leto_mes_obr;
    }
    public void setLetoMesObr(String newLetoMesObr) {
        this.leto_mes_obr = newLetoMesObr;
    }

    public int getPeridika_kreirana() {
        return periodika_kreirana;
    }
    public void setPeridika_kreirana(int newperidika_kreirana) {
        this.periodika_kreirana = newperidika_kreirana;
    }

    public int getPrenos_per() {
        return prenos_per;
    }
    public void setPrenos_per(int newprenos_per) {
        this.prenos_per = newprenos_per;
    }

    public int getSis_pozar() {
        return sis_pozar;
    }
    public void setSis_pozar(int newSisPozar) {
        this.sis_pozar = newSisPozar;
    }

    public int getSis_co() {
        return sis_co;
    }
    public void setSis_co(int newSisCo) {
        this.sis_co = newSisCo;
    }

    public int getSis_vlom() {
        return sis_vlom;
    }
    public void setSis_vlom(int newSisVlom) {
        this.sis_vlom = newSisVlom;
    }

    public int getSis_video() {
        return sis_video;
    }
    public void setSis_video(int newSisVideo) { this.sis_video = newSisVideo;  }

    public int getSis_pristopna() { return sis_pristopna;  }
    public void setSis_pristopna(int newSisPristopna) {
        this.sis_pristopna = newSisPristopna;
    }

    public int getSis_dimni_bankovci() {
        return sis_dimni_bankovci;
    }
    public void setSis_dimni_bankovci(int newSisDim) {
        this.sis_dimni_bankovci = newSisDim;
    }

    public int getPeriodikaDni() {return periodikaDni; }
    public void setPeriodika_dni(int newPerDni) {
        this.periodikaDni = newPerDni;
    }

    public String getTelefon(){return telefon;}
    public void setTelefon(String newTel){this.telefon = newTel;}

    public String getNaslov(){return naslov;}
    public void setNaslov(String newNaslov){this.naslov = newNaslov;}

    public int getSintal_vzd_dn_idid() {
        return sintal_vzd_dn_id;
    }
    public void setSintal_vzd_dn_id(int newId) {
        this.sintal_vzd_dn_id = newId;
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
        return st_del_naloga;
    }
    public void setDelovniNalog(String newDelovniNalog) {
        this.st_del_naloga = newDelovniNalog;
    }

    public String getNaziv_servisa() {
        return naziv_servisa;
    }
    public void setNaziv_servisa(String newNaziv) {
        this.naziv_servisa = newNaziv;
    }

    public String getOpomba() {
        return opomba;
    }
    public void setOpomba(String newOpomba) {
        this.opomba = newOpomba;
    }

    public String getKontaktna_oseba() {
        return kontaktna_oseba;
    }
    public void setKontaktna_oseba(String newKontakt) {
        this.kontaktna_oseba = newKontakt;
    }

    public String getR_objekt_id() {
        return r_objekt_id;
    }
    public void setR_objekt_id(String newOE) {
        this.r_objekt_id = newOE;
    }

    public String getDatum_zadnjega() {
        //return datumZacet
        String vrniStr = "";
        if (datum_zadnjega.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datum_zadnjega);
        }
        return vrniStr;
    }
    public void setDatum_zadnjega(String newDatumZacetek) {
        this.datum_zadnjega = newDatumZacetek;
    }

    public String getDatum_naslednjega() {
        //return datumKonec;
        String vrniStr = "";
        if (datum_naslednjega == null)
        {
            vrniStr = "";
        }
        else if (datum_naslednjega.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datum_naslednjega);
        }
        return vrniStr;
    }
    public void setDatum_naslednjega(String newDatumKonec) {
        this.datum_naslednjega = newDatumKonec;
    }


    public String getDATUM_IZVEDBE() {
        //return datumKonec;
        String vrniStr = "";
        if (DATUM_IZVEDBE == null)
        {
            vrniStr = "";
        }
        else if (DATUM_IZVEDBE.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(DATUM_IZVEDBE);
        }
        return vrniStr;
    }
    public void setDATUM_IZVEDBE(String newDatumIZV) {
        this.DATUM_IZVEDBE = newDatumIZV;
    }

    public int getServiser_izvajalec() {
        return serviser_izvajalec;
    }
    public void setServiser_izvajalec(int newId) {
        this.serviser_izvajalec = newId;
    }


    public void setStatus(int newStatus) { this.status = newStatus; }
    public int getStatus()  { return status; }

    public String getIme() {
        return ime;
    }
    public void setIme(String newIme) {
        this.ime = newIme;
    }

    public String getMc_ime_objekta() {
        return mc_ime_objekta;
    }
    public void setMc_ime_objekta(String newImeObjekta) {
        this.mc_ime_objekta = newImeObjekta;
    }

    public String getNarocnik() {
        return narocnik;
    }
    public void setNarocnik(String newNarocnik) {
        this.narocnik = newNarocnik;
    }

    public String getNarocnikNaslov() {
        return narocnik_naslov;
    }
    public void setNarocnikNaslov(String newNarocnikNaslov) {
        this.narocnik_naslov = newNarocnikNaslov;
    }

    public String getObjekt() {
        return objekt;
    }
    public void setObjekt(String newObjekt) {
        this.objekt = newObjekt;
    }

    public String getObjektNaslov() {
        return objekt_naslov;
    }
    public void setObjektNaslov(String newObjektNaslov) {
        this.objekt_naslov = newObjektNaslov;
    }

    public void setDatumPodpisa(String newDatumPodpisa) {this.datumPodpisa = newDatumPodpisa;  }

    public byte[] getPodpisNarocnik() {
        return podpis_narocnik;
    }
    public void setPodpisNarocnika(byte[] newPodpis) {this.podpis_narocnik = newPodpis;  }

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
