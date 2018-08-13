package de.abas.erp.infosystem.owpdm.pdmbomdiff;

import java.math.BigDecimal;

import de.abas.erp.common.type.enums.EnumDatabase;
import de.abas.erp.common.type.enums.EnumMaterialProvidedTransition;
import de.abas.erp.common.type.enums.EnumUnitForTransitionPeriod;
import de.abas.erp.common.type.enums.EnumVariant;
import de.abas.erp.db.infosystem.custom.owpdm.PdmBOMDifferences;
import de.abas.erp.db.schema.capacity.WorkCenter;
import de.abas.erp.db.schema.filter.Filter;
import de.abas.erp.db.schema.referencetypes.ProductionListItem;
import de.abas.erp.db.type.AbasUnit;

public class PdmBOMDifferencesRow {
	private int stdpdmpos;
	private String stdpdmzid;
	private String flzidstd;
	private String vposstd;
	private ProductionListItem elexstd;
	private BigDecimal lgestd;
	private AbasUnit ellmestd;
	private BigDecimal breitestd;
	private AbasUnit elbmestd;
	private BigDecimal anzahlstd;
	private AbasUnit mellestd;
	private BigDecimal nutzenstd;
	private EnumMaterialProvidedTransition bustd;
	private WorkCenter mgrstd;
	private int lgrstd;
	private String elnamestd;
	private Filter filterstd;
	private String filtervglstd;
	private String filtervgl2std;
	private String ptextstd;
	private BigDecimal pverluststd;
	private BigDecimal amgestd;
	private EnumVariant varstd;
	private int pzeitstd;
	private EnumUnitForTransitionPeriod pzeitlestd;
	private int tlzeitstd;
	private EnumUnitForTransitionPeriod tlzeitlestd;
	private boolean Lfbeiststd;
	private BigDecimal mzrsekstd;
	private AbasUnit Mzrstd;
	private BigDecimal Mzesekstd;
	private AbasUnit Mzestd;
	private Boolean Tersatzstd;
	private Boolean Tverschltstd;
	private Boolean Tnwpflichtstd;
	private Boolean Tserarchivstd;
	private String Ybelaststd;

	private int pdmpos;
	private String pdmzid;
	private String flzid;
	private String vpos;
	private ProductionListItem elex;
	private BigDecimal lge;
	private AbasUnit ellme;
	private BigDecimal breite;
	private AbasUnit elbme;
	private BigDecimal anzahl;
	private AbasUnit melle;
	private BigDecimal nutzen;
	private EnumMaterialProvidedTransition bu;
	private WorkCenter mgr;
	private int lgr;
	private String elname;
	private Filter filter;
	private String filtervgl;
	private String filtervgl2;
	private String ptext;
	private BigDecimal pverlust;
	private BigDecimal amge;
	private EnumVariant var;
	private int pzeit;
	private EnumUnitForTransitionPeriod pzeitle;
	private int tlzeit;
	private EnumUnitForTransitionPeriod tlzeitle;
	private boolean Lfbeist;
	private BigDecimal mzrsek;
	private AbasUnit Mzr;
	private BigDecimal Mzesek;
	private AbasUnit Mze;
	private Boolean Tersatz;
	private Boolean Tverschlt;
	private Boolean Tnwpflicht;
	private Boolean Tserarchiv;
	private String Ybelast;

	private int diffcode;

	private Integer sortPdmPos;
	private BigDecimal vkpverlust;
	private BigDecimal vkpverluststd;

	public PdmBOMDifferencesRow(PdmBOMDifferences.Row pdmBomdiffrow) {
		this.stdpdmpos = pdmBomdiffrow.getStdpdmpos();
		this.pdmpos = pdmBomdiffrow.getPdmpos();

		this.stdpdmzid = pdmBomdiffrow.getStdpdmzid();
		this.pdmzid = pdmBomdiffrow.getPdmzid();

		this.flzidstd = pdmBomdiffrow.getFlzidstd();
		this.flzid = pdmBomdiffrow.getFlzid();

		this.vposstd = pdmBomdiffrow.getVposstd();
		this.vpos = pdmBomdiffrow.getVpos();

		this.elexstd = pdmBomdiffrow.getElexstd();
		this.elex = pdmBomdiffrow.getElex();

		this.lgestd = pdmBomdiffrow.getLgestd();
		this.lge = pdmBomdiffrow.getLge();

		this.breitestd = pdmBomdiffrow.getBreitestd();
		this.breite = pdmBomdiffrow.getBreite();

		this.elbmestd = pdmBomdiffrow.getElbmestd();
		this.elbme = pdmBomdiffrow.getElbme();

		this.anzahlstd = pdmBomdiffrow.getAnzahlstd();
		this.anzahl = pdmBomdiffrow.getAnzahl();

		this.elbmestd = pdmBomdiffrow.getElbmestd();
		this.elbme = pdmBomdiffrow.getElbme();

		this.mellestd = pdmBomdiffrow.getMellestd();
		this.melle = pdmBomdiffrow.getMelle();

		this.nutzenstd = pdmBomdiffrow.getNutzenstd();
		this.nutzen = pdmBomdiffrow.getNutzen();

		this.bustd = pdmBomdiffrow.getBustd();
		this.bu = pdmBomdiffrow.getBu();

		this.mgrstd = pdmBomdiffrow.getMgrstd();
		this.mgr = pdmBomdiffrow.getMgr();

		this.lgrstd = pdmBomdiffrow.getLgrstd();
		this.lgr = pdmBomdiffrow.getLgr();

		this.elnamestd = pdmBomdiffrow.getElnamestd();
		this.elname = pdmBomdiffrow.getElname();

		this.filterstd = pdmBomdiffrow.getFilterstd();
		this.filter = pdmBomdiffrow.getFilter();

		this.filtervglstd = pdmBomdiffrow.getFiltervglstd();
		this.filtervgl = pdmBomdiffrow.getFiltervgl();

		this.filtervgl2std = pdmBomdiffrow.getFiltervgl2std();
		this.filtervgl2 = pdmBomdiffrow.getFiltervgl2();

		this.ptextstd = pdmBomdiffrow.getPtextstd();
		this.ptext = pdmBomdiffrow.getPtext();

		this.pverluststd = pdmBomdiffrow.getPverluststd();
		this.pverlust = pdmBomdiffrow.getPverlust();

		this.vkpverluststd = pdmBomdiffrow.getVkpverluststd();
		this.vkpverlust = pdmBomdiffrow.getVkpverlust();

		this.amgestd = pdmBomdiffrow.getAmgestd();
		this.amge = pdmBomdiffrow.getAmge();

		this.varstd = pdmBomdiffrow.getVarstd();
		this.var = pdmBomdiffrow.getVar();

		this.pzeitstd = pdmBomdiffrow.getPzeitstd();
		this.pzeit = pdmBomdiffrow.getPzeit();

		this.pzeitlestd = pdmBomdiffrow.getPzeitlestd();
		this.pzeitle = pdmBomdiffrow.getPzeitle();

		this.tlzeitstd = pdmBomdiffrow.getTlzeitstd();
		this.tlzeit = pdmBomdiffrow.getTlzeit();

		this.tlzeitlestd = pdmBomdiffrow.getTlzeitlestd();
		this.tlzeitle = pdmBomdiffrow.getTlzeitle();

		this.Lfbeiststd = pdmBomdiffrow.getLfbeiststd();
		this.Lfbeist = pdmBomdiffrow.getLfbeist();

		this.mzrsekstd = pdmBomdiffrow.getMzrsekstd();
		this.mzrsek = pdmBomdiffrow.getMzrsek();

		this.Mzrstd = pdmBomdiffrow.getMzrstd();
		this.Mzr = pdmBomdiffrow.getMzr();

		this.Mzesekstd = pdmBomdiffrow.getMzesekstd();
		this.Mzesek = pdmBomdiffrow.getMzesek();

		this.Mzestd = pdmBomdiffrow.getMzestd();
		this.Mze = pdmBomdiffrow.getMze();

		this.Tersatzstd = pdmBomdiffrow.getTersatzstd();
		this.Tersatz = pdmBomdiffrow.getTersatz();

		this.Tverschltstd = pdmBomdiffrow.getTverschltstd();
		this.Tverschlt = pdmBomdiffrow.getTverschlt();

		this.Tnwpflichtstd = pdmBomdiffrow.getTnwpflichtstd();
		this.Tnwpflicht = pdmBomdiffrow.getTnwpflicht();

		this.Tserarchivstd = pdmBomdiffrow.getTserarchivstd();
		this.Tserarchiv = pdmBomdiffrow.getTserarchiv();

		this.Ybelaststd = pdmBomdiffrow.getYbelaststd();
		this.Ybelast = pdmBomdiffrow.getYbelast();

		this.diffcode = pdmBomdiffrow.getTdiffcode();

		fillSortPdmPos(pdmBomdiffrow);

	}

	private void fillSortPdmPos(PdmBOMDifferences.Row pdmBomdiffrow) {
		EnumDatabase dbno = null;
		if (this.elexstd != null) {
			dbno = this.elexstd.getDBNo();
		} else if (this.elex != null) {
			dbno = this.elex.getDBNo();
		}

		if (dbno != null) {
			if (dbno.equals(EnumDatabase.Operation)) {
				this.sortPdmPos = 10000 + pdmBomdiffrow.getRowNo();
			} else if (this.stdpdmpos == 0 && this.pdmpos == 0) {
				this.sortPdmPos = pdmBomdiffrow.getRowNo();
			} else if (this.stdpdmpos == 0 && this.pdmpos != 0) {
				this.sortPdmPos = pdmBomdiffrow.getPdmpos();
			} else if (this.stdpdmpos != 0 && this.pdmpos == 0) {
				this.sortPdmPos = pdmBomdiffrow.getStdpdmpos();
			} else if (this.stdpdmpos != 0 && this.pdmpos != 0) {
				this.sortPdmPos = pdmBomdiffrow.getPdmpos();
				System.out.println(this.sortPdmPos);
			}
		}
	}

	public Integer getSortpdmpos() {
		return this.sortPdmPos;
	}

	public int getStdpdmpos() {
		return stdpdmpos;
	}

	public String getStdpdmzid() {
		return stdpdmzid;
	}

	public String getFlzidstd() {
		return flzidstd;
	}

	public String getVposstd() {
		return vposstd;
	}

	public ProductionListItem getElexstd() {
		return elexstd;
	}

	public BigDecimal getLgestd() {
		return lgestd;
	}

	public AbasUnit getEllmestd() {
		return ellmestd;
	}

	public BigDecimal getBreitestd() {
		return breitestd;
	}

	public AbasUnit getElbmestd() {
		return elbmestd;
	}

	public BigDecimal getAnzahlstd() {
		return anzahlstd;
	}

	public AbasUnit getMellestd() {
		return mellestd;
	}

	public BigDecimal getNutzenstd() {
		return nutzenstd;
	}

	public EnumMaterialProvidedTransition getBustd() {
		return bustd;
	}

	public WorkCenter getMgrstd() {
		return mgrstd;
	}

	public int getLgrstd() {
		return lgrstd;
	}

	public String getElnamestd() {
		return elnamestd;
	}

	public Filter getFilterstd() {
		return filterstd;
	}

	public String getFiltervglstd() {
		return filtervglstd;
	}

	public String getFiltervgl2std() {
		return filtervgl2std;
	}

	public String getPtextstd() {
		return ptextstd;
	}

	public BigDecimal getPverluststd() {
		return pverluststd;
	}

	public BigDecimal getAmgestd() {
		return amgestd;
	}

	public EnumVariant getVarstd() {
		return varstd;
	}

	public int getPzeitstd() {
		return pzeitstd;
	}

	public EnumUnitForTransitionPeriod getPzeitlestd() {
		return pzeitlestd;
	}

	public int getTlzeitstd() {
		return tlzeitstd;
	}

	public EnumUnitForTransitionPeriod getTlzeitlestd() {
		return tlzeitlestd;
	}

	public boolean isLfbeiststd() {
		return Lfbeiststd;
	}

	public BigDecimal getMzrsekstd() {
		return mzrsekstd;
	}

	public AbasUnit getMzrstd() {
		return Mzrstd;
	}

	public BigDecimal getMzesekstd() {
		return Mzesekstd;
	}

	public AbasUnit getMzestd() {
		return Mzestd;
	}

	public Boolean getTersatzstd() {
		return Tersatzstd;
	}

	public Boolean getTverschltstd() {
		return Tverschltstd;
	}

	public Boolean getTnwpflichtstd() {
		return Tnwpflichtstd;
	}

	public Boolean getTserarchivstd() {
		return Tserarchivstd;
	}

	public String getYbelaststd() {
		return Ybelaststd;
	}

	public int getPdmpos() {
		return pdmpos;
	}

	public String getPdmzid() {
		return pdmzid;
	}

	public String getFlzid() {
		return flzid;
	}

	public String getVpos() {
		return vpos;
	}

	public ProductionListItem getElex() {
		return elex;
	}

	public BigDecimal getLge() {
		return lge;
	}

	public AbasUnit getEllme() {
		return ellme;
	}

	public BigDecimal getBreite() {
		return breite;
	}

	public AbasUnit getElbme() {
		return elbme;
	}

	public BigDecimal getAnzahl() {
		return anzahl;
	}

	public AbasUnit getMelle() {
		return melle;
	}

	public BigDecimal getNutzen() {
		return nutzen;
	}

	public EnumMaterialProvidedTransition getBu() {
		return bu;
	}

	public WorkCenter getMgr() {
		return mgr;
	}

	public int getLgr() {
		return lgr;
	}

	public String getElname() {
		return elname;
	}

	public Filter getFilter() {
		return filter;
	}

	public String getFiltervgl() {
		return filtervgl;
	}

	public String getFiltervgl2() {
		return filtervgl2;
	}

	public String getPtext() {
		return ptext;
	}

	public BigDecimal getPverlust() {
		return pverlust;
	}

	public BigDecimal getAmge() {
		return amge;
	}

	public EnumVariant getVar() {
		return var;
	}

	public int getPzeit() {
		return pzeit;
	}

	public EnumUnitForTransitionPeriod getPzeitle() {
		return pzeitle;
	}

	public int getTlzeit() {
		return tlzeit;
	}

	public EnumUnitForTransitionPeriod getTlzeitle() {
		return tlzeitle;
	}

	public boolean isLfbeist() {
		return Lfbeist;
	}

	public BigDecimal getMzrsek() {
		return mzrsek;
	}

	public AbasUnit getMzr() {
		return Mzr;
	}

	public BigDecimal getMzesek() {
		return Mzesek;
	}

	public AbasUnit getMze() {
		return Mze;
	}

	public Boolean getTersatz() {
		return Tersatz;
	}

	public Boolean getTverschlt() {
		return Tverschlt;
	}

	public Boolean getTnwpflicht() {
		return Tnwpflicht;
	}

	public Boolean getTserarchiv() {
		return Tserarchiv;
	}

	public String getYbelast() {
		return Ybelast;
	}

	public int getDiffcode() {
		return diffcode;
	}

	public BigDecimal getVkpverlust() {
		return vkpverlust;
	}

	public BigDecimal getVkpverluststd() {
		return vkpverluststd;
	}

	public Integer getSortPdmPos() {
		return sortPdmPos;
	}

}
