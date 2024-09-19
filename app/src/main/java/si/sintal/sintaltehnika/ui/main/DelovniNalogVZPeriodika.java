package si.sintal.sintaltehnika.ui.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DelovniNalogVZPeriodika {

    int id;
    //int sintal_vzd_dn_id;
    String st_del_naloga;
    int tipNarocila;

    int sis_pozar;
    int sis_vlom;
    int sis_video;
    int sis_co;
    int sis_pristopna;
    int sis_dimni_bankovci;
    int sis_ostalo;
    int periodikaDni;
    int redno;
    int izredno;

    String tip_elementov;
    String datum_prejsenjega;
    String datum_naslednjega;
    String kontrolor;
    String aku_bat;
    String nacin_prenosa;
    String prenos_inst;
    byte[] podpis_vzdrzevalec;
    byte[] podpis_narocnik;
    int status;
    String DATUM_DODELITVE;
    String DATUM_IZVEDBE;
    int prenos;
    String DATUM_PRENOSA;
    double ureDelo;
    double urePrevoz;
    double stKm;
    String datumPodpisa;
    int serviser_izvajalec;
    String opomba;
    int pr1;
    int pr2;
    int pr3;
    int pr4;
    int pr5;

    int pr6;
    int pr7;
    int pr8;
    int pr9;
    int pr10;

    int pr11;
    int pr12;
    int pr13;
    int pr14;
    int pr15;

    int pr16;
    int pr17;
    int pr18;
    byte[] podpis;

    public int getid() {
        return id;
    }
    public void setid(int newId) {
        this.id = newId;
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

    public String getDatum_prejsnjega() {
        //return datumZacet
        String vrniStr = "";
        if (datum_prejsenjega.equals("null"))
        {
            vrniStr = "";
        }
        else {
            vrniStr = vrniDatum(datum_prejsenjega);
        }
        return vrniStr;
    }
    public void setDatum_prejsenjega(String newDatumZacetek) {
        this.datum_prejsenjega = newDatumZacetek;
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

    public void setDatumPodpisa(String newDatumPodpisa) {this.datumPodpisa = newDatumPodpisa;  }

    public byte[] getPodpisNarocnik() {
        return podpis_narocnik;
    }
    public void setPodpisNarocnika(byte[] newPodpis) {this.podpis_narocnik = newPodpis;  }

    public String vrniDatum(String datumDb)
    {
        int ena = 0;
        String vrniStr = "";
        try {
            String string_format = datumDb;
            Date date_format = new SimpleDateFormat("yyyy-MM-dd").parse(string_format);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            vrniStr = dateFormat.format(date_format);
            ena = 1;
        }
        catch (Exception e)
        {
            vrniStr = "";
        }
        if (ena == 0) {
            try {
                String string_format = datumDb;
                Date date_format = new SimpleDateFormat("dd.MM.yyyy").parse(string_format);
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                vrniStr = string_format;
            } catch (Exception e) {
                vrniStr = "";
            }
        }
        return vrniStr;
    }

    public int getTipNarocila() {
        return tipNarocila;
    }
    public void setTipNarocila(int newTipNarocila) {
        this.tipNarocila = newTipNarocila;
    }



    public int getPr1() {
        return pr1;
    }
    public void setPr1(int newPr1) {
        this.pr1 = newPr1;
    }

    public int getPr2() {
        return pr2;
    }
    public void setPr2(int newPr2) {
        this.pr2 = newPr2;
    }

    public int getPr3() {
        return pr3;
    }
    public void setPr3(int newPr3) {
        this.pr3 = newPr3;
    }

    public int getPr4() {
        return pr4;
    }
    public void setPr4(int newPr4) {
        this.pr4 = newPr4;
    }

    public int getPr5() {
        return pr5;
    }
    public void setPr5(int newPr5) {
        this.pr5 = newPr5;
    }

    public int getPr6() {
        return pr6;
    }
    public void setPr6(int newPr6) {
        this.pr6 = newPr6;
    }

    public int getPr7() {
        return pr7;
    }
    public void setPr7(int newPr7) {
        this.pr7 = newPr7;
    }

    public int getPr8() {
        return pr8;
    }
    public void setPr8(int newPr8) {
        this.pr8 = newPr8;
    }

    public int getPr9() {
        return pr9;
    }
    public void setPr9(int newPr9) {
        this.pr9 = newPr9;
    }

    public int getPr10() {
        return pr10;
    }
    public void setPr10(int newPr10) {
        this.pr10 = newPr10;
    }

    public int getPr11() {
        return pr11;
    }
    public void setPr11(int newPr11) {
        this.pr11 = newPr11;
    }

    public int getPr12() {
        return pr12;
    }
    public void setPr12(int newPr12) {
        this.pr12 = newPr12;
    }

    public int getPr13() {
        return pr13;
    }
    public void setPr13(int newPr13) {
        this.pr13 = newPr13;
    }

    public int getPr14() {
        return pr14;
    }
    public void setPr14(int newPr14) {
        this.pr14 = newPr14;
    }

    public int getPr15() {
        return pr15;
    }
    public void setPr15(int newPr15) {
        this.pr15 = newPr15;
    }

    public int getPr16() {
        return pr16;
    }
    public void setPr16(int newPr16) {
        this.pr16 = newPr16;
    }

    public int getPr17() {
        return pr17;
    }
    public void setPr17(int newPr17) {
        this.pr17 = newPr17;
    }

    public int getPr18() {
        return pr18;
    }
    public void setPr18(int newPr18) {
        this.pr18 = newPr18;
    }

    public int getRedno() {
        return redno;
    }
    public void setRedno(int newRedno) {
        this.redno = newRedno;
    }

    public int getIzredno() {
        return izredno;
    }
    public void setIzredno(int newIzredno) {
        this.izredno = newIzredno;
    }


    public String getkontrolor() {
        return kontrolor;
    }
    public void setkontrolor(String newkontrolor) {
        this.kontrolor = newkontrolor;
    }

    public String getaku_bat() {
        return aku_bat;
    }
    public void setaku_bat(String newaku_bat) {
        this.aku_bat = newaku_bat;
    }

    public String getnacin_prenosa() {
        return nacin_prenosa;
    }
    public void setnacin_prenosa(String newnacin_prenosa) {
        this.nacin_prenosa = newnacin_prenosa;
    }

    public String getprenos_inst() {
        return prenos_inst;
    }
    public void setprenos_inst(String newprenos_inst) {
        this.prenos_inst = newprenos_inst;
    }

    public String getTip_elementov() {
        return tip_elementov;
    }
    public void setTip_elementov(String newtip_elementov) {
        this.tip_elementov = newtip_elementov ;
    }

    public String getOpomba() {
        return opomba;
    }
    public void setOpomba(String newopomba) {
        this.opomba = newopomba;
    }

    /*String kontrolor;
    String aku_bat;
    String nacin_prenosa;
    String prenos_inst;*/
    public byte[] getPodpis() {
        return podpis;
    }
    public void setPodpis(byte[] newPodpis) {this.podpis = newPodpis;  }

}
