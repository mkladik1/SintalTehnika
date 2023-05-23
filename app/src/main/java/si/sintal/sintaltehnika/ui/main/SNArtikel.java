package si.sintal.sintaltehnika.ui.main;

public class SNArtikel {
    String id;
    String naziv;
    String nazivIskanje;
    String merskaEnota;
    String kratkaOznaka;
    int snArtikelId;
    int zamenjanNov;

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
}
