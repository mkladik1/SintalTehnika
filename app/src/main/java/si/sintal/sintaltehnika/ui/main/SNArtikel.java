package si.sintal.sintaltehnika.ui.main;

public class SNArtikel {
    String id;
    String naziv;
    String nazivIskanje;
    String merskaEnota;
    String kratkaOznaka;
    int snArtikelId;
    int zamenjanNov;
    float kolicina;
    String regal;
    int sn_id;
    int sn_artikel_id;
    String delovniNalog;
    String upoId;
    String tehnikId;

    public void  setTehnikId(String newUpiId){ this.tehnikId = newUpiId;}

    public String getTehnikId() { return tehnikId;}

    public void  setUpoId(String newUpiId){ this.upoId = newUpiId;}

    public String getUpoId() { return upoId;}

    public String getDelovniNalog() {return delovniNalog;}

    public void setDelovniNalog(String newDelovniNalog){ this.delovniNalog = newDelovniNalog;}

    public int getSn_id() {return sn_id;}

    public void setSn_id(int newSndId) {this.sn_id = newSndId;}

    public int getSnArtikelId(){ return sn_artikel_id;}

    public void setSn_artikel_id( int newSnArtikelId) {this.sn_artikel_id = newSnArtikelId;}

    public String getid() {
        return id;
    }

    public void setid(String newId) {
        this.id = newId;
    }

    public String getnaziv() {
        return naziv;
    }

    public void setnaziv(String newNaziv) {
        this.naziv = newNaziv;
    }

    public String getnazivIskanje() {
        return nazivIskanje;
    }

    public void setnazivIskanje(String newNazivIskanje) {
        this.nazivIskanje = newNazivIskanje;
    }

    public String getmerskaEnota() {
        return merskaEnota;
    }

    public void setmerskaEnota(String newMerskaEnota) {
        this.merskaEnota = newMerskaEnota;
    }

    public String getKratkaOznaka() {
        return kratkaOznaka;
    }

    public void setKratkaOznaka(String newKratkaOznaka) {
        this.kratkaOznaka = newKratkaOznaka;
    }

    public int getSNArtikelId(){ return snArtikelId;}

    public void setSNArtikelId(int newArtikelId){ this.snArtikelId = newArtikelId;}

    public int getZamenjanNov() {
        return zamenjanNov;
    }
    public void setZamenjanNov(int newZamenjanNov){this.zamenjanNov = newZamenjanNov;}

    public float getKolicina() {
        return kolicina;
    }

    public void setKolicina(float newKolicina){this.kolicina = newKolicina;}

    public String getRegal(){ return regal;}

    public void setRegal(String newRegal){this.regal = newRegal;}

}
