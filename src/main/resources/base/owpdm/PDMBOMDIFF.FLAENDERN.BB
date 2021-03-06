..!interpreter english translate noabbrev
..*****************************************************************************
.. Name           : PDMBOMDIFF.FLAENDERN.BB
.. Author         : cl
.. Verantwortlich : cl
.. Kontrolle      : fwester
.. Functionality  : Selects a production list using a selction bar.
..*****************************************************************************
.set trans -
..
.continue ERROR01 ? F|empty(M|artikel)
..
.formula M|select = "%,,"
.formula M|select = M|select + ";.artikel=" + M|artikel^id
.formula M|select = M|select + ";such=;nummer=;lgruppe="
.formula M|select = M|select + ";.flistestd=nein"
.formula M|select = M|select + ";@gruppe=1"
.formula M|select = M|select + ";@ordnung=nummer;@richtung=r�ckw�rts"
.formula M|select = M|select + ";@ablageart=lebendig"
.load 0 productionlist 'M|select'
.continue ERROR02 ? _G|mehr
.assign M|fliste = 0|id
.formula M|nrfliste = 0|nummer
.formula M|flbasis = 0|flbasis
.formula M|le = M|artikel^le
..*****************************************************************************
!END
.continue
..*****************************************************************************
.set trans +
..
!ERROR01: Missing product.
.error -field M|artikel -message Bitte den Artikel eintragen!
.end 1
..
!ERROR02: Break selection bar.
.error -field M|flaendern -message Vorgang wurde abgebrochen.
.end 1
