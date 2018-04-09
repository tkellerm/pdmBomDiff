/*-----------------------------------------------------------------------------
 * Modul Name       : PdmBomDiff.java
 * Autor            : dsch
 * Verantwortlich   : dsch
 * Kontrolle        : mh
 *---------------------------------------------------------------------------*/
package de.abas.erp.infosystem.owpdm.pdmbomdiff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.abas.eks.jfop.remote.FO;
import de.abas.erp.axi.event.ButtonEvent;
import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.event.ObjectEventHandler;
import de.abas.erp.axi.event.RecordEventHandler;
import de.abas.erp.axi.event.RowEventHandler;
import de.abas.erp.axi.event.listener.ButtonListenerAdapter;
import de.abas.erp.db.field.editable.ButtonField;
import de.abas.erp.db.infosystem.custom.owpdm.PdmBOMDifferences;
import de.abas.erp.db.infosystem.custom.owpdm.PdmBOMDifferences.Row;
import de.abas.erp.db.infosystem.custom.owpdm.PdmBOMDifferences.Table;
import de.abas.erp.db.schema.operation.Operation;
import de.abas.erp.db.schema.part.MeansOfProduction;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.Service;
import de.abas.erp.db.schema.productionlist.ProductionList;
import de.abas.utils.diff.CommonSequences;
import de.abas.utils.diff.EnumSequenceType;
import de.abas.utils.diff.Sequence;

/**
 * This class contains the button events for info system PdmBomDiff
 * 
 * @author dsch
 */
public class PdmBomDiff extends RowEventHandler<PdmBOMDifferences, PdmBOMDifferences.Row> {

	private String colorBOM1Diff;
	private String colorBOM2Diff;

	private static final String[] BOM1_FIELD_LIST = { PdmBOMDifferences.Row.META.stdpdmpos.getName(),
			PdmBOMDifferences.Row.META.stdpdmzid.getName(), PdmBOMDifferences.Row.META.vposstd.getName(),
			PdmBOMDifferences.Row.META.elexstd.getName(), PdmBOMDifferences.Row.META.elnamestd.getName(),
			PdmBOMDifferences.Row.META.lgestd.getName(), PdmBOMDifferences.Row.META.ellmestd.getName(),
			PdmBOMDifferences.Row.META.breitestd.getName(), PdmBOMDifferences.Row.META.elbmestd.getName(),
			PdmBOMDifferences.Row.META.anzahlstd.getName(), PdmBOMDifferences.Row.META.mellestd.getName(),
			PdmBOMDifferences.Row.META.nutzenstd.getName(), PdmBOMDifferences.Row.META.bustd.getName(),
			PdmBOMDifferences.Row.META.mgrstd.getName(), PdmBOMDifferences.Row.META.lgrstd.getName(),
			PdmBOMDifferences.Row.META.ybelaststd.getName() };

	private static final String[] BOM2_FIELD_LIST = { PdmBOMDifferences.Row.META.pdmpos.getName(),
			PdmBOMDifferences.Row.META.pdmzid.getName(), PdmBOMDifferences.Row.META.vpos.getName(),
			PdmBOMDifferences.Row.META.elex.getName(), PdmBOMDifferences.Row.META.elname.getName(),
			PdmBOMDifferences.Row.META.lge.getName(), PdmBOMDifferences.Row.META.ellme.getName(),
			PdmBOMDifferences.Row.META.breite.getName(), PdmBOMDifferences.Row.META.elbme.getName(),
			PdmBOMDifferences.Row.META.anzahl.getName(), PdmBOMDifferences.Row.META.melle.getName(),
			PdmBOMDifferences.Row.META.nutzen.getName(), PdmBOMDifferences.Row.META.bu.getName(),
			PdmBOMDifferences.Row.META.mgr.getName(), PdmBOMDifferences.Row.META.lgr.getName(),
			PdmBOMDifferences.Row.META.ybelast.getName() };

	private int index1 = -1;
	private int index2 = -1;
	private int indexInsert = 1;

	/**
	 * Constructor for handling info system header and info system row events
	 * for info system PdmBOMDifferences.
	 */
	public PdmBomDiff() {
		super(PdmBOMDifferences.class, PdmBOMDifferences.Row.class);
	}

	/**
	 * Register events for buttons in head of info system.
	 * 
	 * <li><b>bstart:</b> shows the schema structure.</li>
	 * 
	 * <li><b>erg:</b> shows the field relations between two schemas.</li>
	 * 
	 * @author dsch
	 */
	class HeadButtonListener extends ButtonListenerAdapter<PdmBOMDifferences> {

		private final ButtonField<PdmBOMDifferences> field;

		public HeadButtonListener(final ButtonField<PdmBOMDifferences> field) {
			this.field = field;
		}

		@Override
		public void after(final ButtonEvent<PdmBOMDifferences> event) throws EventException {

			super.after(event);
			final PdmBOMDifferences pdmbomdiff = getCurrentObject();

			if (this.field.equals(PdmBOMDifferences.META.start)) {
				compareBOM(pdmbomdiff);
				// moveNewRowtoUpper(pdmbomdiff);
				sortRowsWithPdmPosNumber(pdmbomdiff);
			}
		}
	}

	private void sortRowsWithPdmPosNumber(PdmBOMDifferences pdmbomdiff) {
		ArrayList<PdmBOMDifferencesRow> pdmbomdiffrows = new ArrayList<PdmBOMDifferencesRow>();

		Table table = pdmbomdiff.table();
		Iterable<Row> rows = pdmbomdiff.table().getRows();
		for (Row row : rows) {
			PdmBOMDifferencesRow pdmdiffRow = new PdmBOMDifferencesRow(row);
			pdmbomdiffrows.add(pdmdiffRow);
		}

		pdmbomdiffrows.sort(new PdmBomDiffRowComparator());

		pdmbomdiff.table().clear();

		insertTableRows(pdmbomdiffrows, pdmbomdiff);

		colorTable(pdmbomdiff);

	}

	private void colorTable(PdmBOMDifferences pdmbomdiff) {
		Iterable<Row> rows = pdmbomdiff.table().getRows();

		for (Row row : rows) {
			resetBOMFieldsColor(row, BOM1_FIELD_LIST);
			resetBOMFieldsColor(row, BOM2_FIELD_LIST);

			switch (row.getTdiffcode()) {
			case 1:
				compareElementAttributes(row);
			case 2:
				setBOMFieldsColor(row, BOM1_FIELD_LIST, colorBOM1Diff);
				break;

			case 3:
				setBOMFieldsColor(row, BOM2_FIELD_LIST, colorBOM2Diff);
				break;

			default:
				break;
			}

		}

	}

	private void compareElementAttributes(Row row) {

		// compare production list row attributes
		if (row.getStdpdmpos() != row.getPdmpos()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.stdpdmpos.getName(),
					PdmBOMDifferences.Row.META.pdmpos.getName());

		}
		if (row.getLgestd().compareTo(row.getLge()) != 0) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.lgestd.getName(), PdmBOMDifferences.Row.META.lge.getName());

		}
		if (row.getEllmestd() != row.getEllme()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.ellmestd.getName(),
					PdmBOMDifferences.Row.META.ellme.getName());

		}
		if (row.getBreitestd().compareTo(row.getBreite()) != 0) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.breitestd.getName(),
					PdmBOMDifferences.Row.META.breite.getName());

		}
		if (row.getElbmestd() != row.getElbme()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.elbmestd.getName(),
					PdmBOMDifferences.Row.META.elbme.getName());

		}
		if (row.getAnzahlstd().compareTo(row.getAnzahl()) != 0) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.anzahlstd.getName(),
					PdmBOMDifferences.Row.META.anzahl.getName());

		}
		if (row.getMellestd() != row.getMelle()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.mellestd.getName(),
					PdmBOMDifferences.Row.META.melle.getName());

		}
		if (row.getNutzenstd().compareTo(row.getNutzen()) != 0) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.nutzenstd.getName(),
					PdmBOMDifferences.Row.META.nutzen.getName());

		}
		if (row.getBustd() != row.getBu()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.bustd.getName(), PdmBOMDifferences.Row.META.bu.getName());

		}
		if (row.getMgrstd() != row.getMgr()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.mgrstd.getName(), PdmBOMDifferences.Row.META.mgr.getName());

		}
		if (row.getLgrstd() != row.getLgr()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.lgrstd.getName(), PdmBOMDifferences.Row.META.lgr.getName());

		}
		// Änderung Romaco
		if (row.getYbelaststd() != row.getYbelast()) {
			setFieldsColor(row, PdmBOMDifferences.Row.META.ybelaststd.getName(),
					PdmBOMDifferences.Row.META.ybelast.getName());

		}

	}

	private void insertTableRows(ArrayList<PdmBOMDifferencesRow> pdmbomdiffrows, PdmBOMDifferences pdmbomdiff) {

		for (PdmBOMDifferencesRow pdmBOMDifferencesRow : pdmbomdiffrows) {
			insertTableRow(pdmBOMDifferencesRow, pdmbomdiff);
		}

	}

	private void insertTableRow(PdmBOMDifferencesRow pdmBOMDifferencesRow, PdmBOMDifferences pdmbomdiff) {
		final PdmBOMDifferences.Row row;

		row = pdmbomdiff.table().appendRow();

		row.setStdpdmpos(pdmBOMDifferencesRow.getStdpdmpos());
		row.setStdpdmzid(pdmBOMDifferencesRow.getStdpdmzid());
		row.setFlzidstd(pdmBOMDifferencesRow.getFlzidstd());
		row.setVposstd(pdmBOMDifferencesRow.getVposstd());
		row.setElexstd(pdmBOMDifferencesRow.getElexstd());
		row.setLgestd(pdmBOMDifferencesRow.getLgestd());
		if (pdmBOMDifferencesRow.getEllmestd() != null) {
			row.setEllmestd(pdmBOMDifferencesRow.getEllmestd());
		}
		row.setBreitestd(pdmBOMDifferencesRow.getBreitestd());
		if (pdmBOMDifferencesRow.getElbmestd() != null) {
			row.setElbmestd(pdmBOMDifferencesRow.getElbmestd());
		}
		row.setAnzahlstd(pdmBOMDifferencesRow.getAnzahlstd());
		if (pdmBOMDifferencesRow.getMellestd() != null) {
			row.setMellestd(pdmBOMDifferencesRow.getMellestd());
		}
		row.setNutzenstd(pdmBOMDifferencesRow.getNutzenstd());
		row.setBustd(pdmBOMDifferencesRow.getBustd());
		row.setMgrstd(pdmBOMDifferencesRow.getMgrstd());
		row.setLgrstd(pdmBOMDifferencesRow.getLgrstd());

		row.setElnamestd(pdmBOMDifferencesRow.getElnamestd());

		row.setFilterstd(pdmBOMDifferencesRow.getFilterstd());
		row.setFiltervglstd(pdmBOMDifferencesRow.getFiltervglstd());
		row.setFiltervgl2std(pdmBOMDifferencesRow.getFiltervgl2std());
		row.setPtextstd(pdmBOMDifferencesRow.getPtextstd());
		row.setPverluststd(pdmBOMDifferencesRow.getPverluststd());

		row.setVkpverluststd(pdmBOMDifferencesRow.getVkpverluststd());

		row.setAmgestd(pdmBOMDifferencesRow.getAmgestd());
		row.setVarstd(pdmBOMDifferencesRow.getVarstd());
		row.setPzeitstd(pdmBOMDifferencesRow.getPzeitstd());
		if (pdmBOMDifferencesRow.getPzeitlestd() != null) {
			row.setPzeitlestd(pdmBOMDifferencesRow.getPzeitlestd());
		}
		row.setTlzeitstd(pdmBOMDifferencesRow.getTlzeitstd());
		if (pdmBOMDifferencesRow.getTlzeitlestd() != null) {
			row.setTlzeitlestd(pdmBOMDifferencesRow.getTlzeitlestd());
		}
		row.setLfbeiststd(pdmBOMDifferencesRow.isLfbeiststd());
		row.setMzrsekstd(pdmBOMDifferencesRow.getMzrsekstd());
		if (pdmBOMDifferencesRow.getMzrstd() != null) {
			row.setMzrstd(pdmBOMDifferencesRow.getMzrstd());
		}
		row.setMzesekstd(pdmBOMDifferencesRow.getMzesekstd());
		if (pdmBOMDifferencesRow.getMzestd() != null) {
			row.setMzestd(pdmBOMDifferencesRow.getMzestd());
		}
		row.setTersatzstd(pdmBOMDifferencesRow.getTersatzstd());
		row.setTverschltstd(pdmBOMDifferencesRow.getTverschltstd());
		row.setTnwpflichtstd(pdmBOMDifferencesRow.getTnwpflichtstd());
		row.setTserarchivstd(pdmBOMDifferencesRow.getTserarchivstd());
		// Änderung Romaco
		row.setYbelaststd(pdmBOMDifferencesRow.getYbelaststd());

		row.setPdmpos(pdmBOMDifferencesRow.getPdmpos());
		row.setPdmzid(pdmBOMDifferencesRow.getPdmzid());
		row.setFlzid(pdmBOMDifferencesRow.getFlzid());
		row.setVpos(pdmBOMDifferencesRow.getVpos());
		row.setElex(pdmBOMDifferencesRow.getElex());
		row.setLge(pdmBOMDifferencesRow.getLge());
		if (pdmBOMDifferencesRow.getEllme() != null) {
			row.setEllme(pdmBOMDifferencesRow.getEllme());
		}
		row.setBreite(pdmBOMDifferencesRow.getBreite());
		if (pdmBOMDifferencesRow.getElbme() != null) {
			row.setElbme(pdmBOMDifferencesRow.getElbme());
		}
		row.setAnzahl(pdmBOMDifferencesRow.getAnzahl());
		if (pdmBOMDifferencesRow.getMelle() != null) {
			row.setMelle(pdmBOMDifferencesRow.getMelle());
		}
		row.setNutzen(pdmBOMDifferencesRow.getNutzen());
		row.setBu(pdmBOMDifferencesRow.getBu());
		row.setMgr(pdmBOMDifferencesRow.getMgr());
		row.setLgr(pdmBOMDifferencesRow.getLgr());

		row.setElname(pdmBOMDifferencesRow.getElname());

		row.setFilter(pdmBOMDifferencesRow.getFilter());
		row.setFiltervgl(pdmBOMDifferencesRow.getFiltervgl());
		row.setFiltervgl2(pdmBOMDifferencesRow.getFiltervgl2());
		row.setPtext(pdmBOMDifferencesRow.getPtext());
		row.setPverlust(pdmBOMDifferencesRow.getPverlust());

		row.setVkpverlust(pdmBOMDifferencesRow.getVkpverlust());
		row.setAmge(pdmBOMDifferencesRow.getAmge());
		row.setVar(pdmBOMDifferencesRow.getVar());
		row.setPzeit(pdmBOMDifferencesRow.getPzeit());
		if (pdmBOMDifferencesRow.getPzeitle() != null) {
			row.setPzeitle(pdmBOMDifferencesRow.getPzeitle());
		}
		row.setTlzeit(pdmBOMDifferencesRow.getTlzeit());
		if (pdmBOMDifferencesRow.getTlzeitle() != null) {
			row.setTlzeitle(pdmBOMDifferencesRow.getTlzeitle());
		}
		row.setLfbeist(pdmBOMDifferencesRow.isLfbeist());
		row.setMzrsek(pdmBOMDifferencesRow.getMzrsek());
		if (pdmBOMDifferencesRow.getMzr() != null) {
			row.setMzr(pdmBOMDifferencesRow.getMzr());
		}
		row.setMzesek(pdmBOMDifferencesRow.getMzesek());
		if (pdmBOMDifferencesRow.getMze() != null) {
			row.setMze(pdmBOMDifferencesRow.getMze());
		}
		row.setTersatz(pdmBOMDifferencesRow.getTersatz());
		row.setTverschlt(pdmBOMDifferencesRow.getTverschlt());
		row.setTnwpflicht(pdmBOMDifferencesRow.getTnwpflicht());
		row.setTserarchiv(pdmBOMDifferencesRow.getTserarchiv());
		// Änderung Romaco
		row.setYbelast(pdmBOMDifferencesRow.getYbelast());

		row.setTdiffcode(pdmBOMDifferencesRow.getDiffcode());

	}

	@Override
	protected void configureEventHandler(final ObjectEventHandler<PdmBOMDifferences> objectHandler,
			final RecordEventHandler<PdmBOMDifferences.Row> rowHandler) {

		super.configureEventHandler(objectHandler, rowHandler);

		objectHandler.addListener(PdmBOMDifferences.META.start, new HeadButtonListener(PdmBOMDifferences.META.start));
	}

	// private void moveNewRowtoUpper(PdmBOMDifferences pdmbomdiff) {
	// Integer index = 1;
	// Table table = pdmbomdiff.table();
	// Iterable<Row> rows = pdmbomdiff.table().getRows();
	// Integer indexLastStdLine = lastStdLine(rows);
	//
	// for (Row row : rows) {
	// if (row.getRowNo() > indexLastStdLine) {
	// table.moveRow(row.getRowNo(), index);
	// index++;
	// }
	// }
	//
	// }

	private Integer lastStdLine(Iterable<Row> rows) {
		Integer rowNumber = 1;
		for (Row row : rows) {
			if (row.getElexstd() != null) {
				rowNumber = row.getRowNo();
			}
		}

		return rowNumber;
	}

	private void compareBOM(PdmBOMDifferences pdmbomdiff) {
		this.indexInsert = 1;
		colorBOM1Diff = pdmbomdiff.getFarbe1();
		colorBOM2Diff = pdmbomdiff.getFarbe2();

		String bom1Nb = pdmbomdiff.getNrflistestd();
		if ("".equals(bom1Nb)) {
			return;
		}
		String bom2Nb = pdmbomdiff.getNrfliste();
		if ("".equals(bom2Nb)) {
			return;
		}
		List<ProductionList.Row> bom1 = new ArrayList<ProductionList.Row>();
		List<ProductionList.Row> bom2 = new ArrayList<ProductionList.Row>();

		ArrayList<String> bom1Tokens = new ArrayList<String>();
		ArrayList<String> bom2Tokens = new ArrayList<String>();

		ProductionList productionList1 = pdmbomdiff.getFlistestd();
		if (productionList1 != null) {
			getProductionListRows(productionList1, bom1, bom1Tokens);
		}

		ProductionList productionList2 = pdmbomdiff.getFliste();
		if (productionList2 != null) {
			getSortedProductionListRows(productionList2, bom2, bom2Tokens, bom1Tokens);
		}
		CommonSequences commonSequences = new CommonSequences(bom1Tokens, bom2Tokens);

		List<Sequence<String>> sequences = commonSequences.diff();

		Queue<String> queue1 = new LinkedList<String>();
		Queue<String> queue2 = new LinkedList<String>();

		this.index1 = -1;
		this.index2 = -1;

		for (int index = 0; index < sequences.size(); index++) {

			Sequence<String> sequence = sequences.get(index);

			if (sequence.getType().equals(EnumSequenceType.COMMON_SEQUENCE)) {

				clearQueues(pdmbomdiff, queue1, queue2, bom1, bom2, false);

				queue1.add(sequence.getValue());
				queue2.add(sequence.getValue());
				clearQueues(pdmbomdiff, queue1, queue2, bom1, bom2, true);

			} else if (sequence.getType().equals(EnumSequenceType.FIRST_SEQUENCE)) {
				queue1.add(sequence.getValue());
				clearQueues(pdmbomdiff, queue1, queue2, bom1, null, false);
			} else {
				queue2.add(sequence.getValue());
				clearQueues(pdmbomdiff, queue1, queue2, null, bom2, false);
			}
		}
	}

	private void getProductionListRows(ProductionList productionList, List<ProductionList.Row> bom,
			List<String> bomTokens) {

		for (ProductionList.Row child : productionList.table().getRows()) {
			bom.add(child);
			// ist die GUID (pdmzid) der Zuordnung gef�llt, wird diese als Basis
			// für den Vergleich genommen,
			// wenn nicht wird die interne ID des Artikels br�cksichtigt
			bomTokens.add(getBomRowToken(child));
		}
	}

	private String getBomRowToken(ProductionList.Row row) {
		if (!row.getPdmRowId().isEmpty()) {
			return row.getPdmRowId();
		}
		return row.getProdListElem().getId().toString();
	}

	private void getSortedProductionListRows(ProductionList productionList, List<ProductionList.Row> bom,
			List<String> bomTokens, List<String> standardBomTokens) {

		for (String baseToken : standardBomTokens) {
			for (ProductionList.Row child : productionList.table().getRows()) {
				String token = getBomRowToken(child);
				if (token.equals(baseToken)) {
					bomTokens.add(token);
					bom.add(child);
				}
			}
		}
		int sortedIndex = 0;
		for (ProductionList.Row child : productionList.table().getRows()) {
			String token = getBomRowToken(child);
			if (standardBomTokens.indexOf(token) == -1) {
				bomTokens.add(sortedIndex, token);
				bom.add(sortedIndex, child);
			}
			sortedIndex++;
		}
	}

	private void clearQueues(PdmBOMDifferences pdmbomdiff, Queue<String> queue1, Queue<String> queue2,
			List<ProductionList.Row> bom1, List<ProductionList.Row> bom2, boolean isCommon) {

		while (!queue1.isEmpty() || !queue2.isEmpty()) {
			String token1 = queue1.poll();
			String token2 = queue2.poll();
			Boolean bom1leer = false;
			ProductionList.Row productionListRow1 = null;
			ProductionList.Row productionListRow2 = null;

			if (token1 != null) {
				productionListRow1 = bom1.get(++this.index1);
			}

			if (token2 != null) {
				productionListRow2 = bom2.get(++this.index2);
			}

			if (bom1 != null) {
				if (this.index1 >= bom1.size()) {
					bom1leer = true;
				}
			} else {
				bom1leer = true;
			}
			PdmBOMDifferences.Row newTableRow = insertTableRow(pdmbomdiff, productionListRow1, productionListRow2,
					bom1leer);

			int codeDifference = 0;
			if (!isCommon) {
				if (token1 != null) {
					setBOMFieldsColor(newTableRow, BOM1_FIELD_LIST, colorBOM1Diff);
					codeDifference = 2;
				} else {
					resetBOMFieldsColor(newTableRow, BOM1_FIELD_LIST);
				}
				if (token2 != null) {
					setBOMFieldsColor(newTableRow, BOM2_FIELD_LIST, colorBOM2Diff);
					codeDifference = 3;
				} else {
					resetBOMFieldsColor(newTableRow, BOM2_FIELD_LIST);
				}
			} else if ((productionListRow1 != null) && (productionListRow2 != null)) {
				resetBOMFieldsColor(newTableRow, BOM1_FIELD_LIST);
				resetBOMFieldsColor(newTableRow, BOM2_FIELD_LIST);
				if (compareElementAttributes(pdmbomdiff, newTableRow, productionListRow1, productionListRow2)) {
					codeDifference = 1;
				}
			}
			newTableRow.setTdiffcode(codeDifference);
		}
	}

	private boolean compareElementAttributes(final PdmBOMDifferences pdmbomdiff,
			final PdmBOMDifferences.Row newTableRow, final ProductionList.Row productionListRow1,
			final ProductionList.Row productionListRow2) {

		boolean hasDifference = false;
		// compare production list row attributes
		if (productionListRow1.getPdmPosNo() != productionListRow2.getPdmPosNo()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.stdpdmpos.getName(),
					PdmBOMDifferences.Row.META.pdmpos.getName());
			hasDifference = true;
		}
		if (productionListRow1.getSetupTime().compareTo(productionListRow2.getSetupTime()) != 0) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.lgestd.getName(),
					PdmBOMDifferences.Row.META.lge.getName());
			hasDifference = true;
		}
		if (productionListRow1.getSetupTimeUnit() != productionListRow2.getSetupTimeUnit()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.ellmestd.getName(),
					PdmBOMDifferences.Row.META.ellme.getName());
			hasDifference = true;
		}
		if (productionListRow1.getTimeLimUnit().compareTo(productionListRow2.getTimeLimUnit()) != 0) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.breitestd.getName(),
					PdmBOMDifferences.Row.META.breite.getName());
			hasDifference = true;
		}
		if (productionListRow1.getTimeUnit() != productionListRow2.getTimeUnit()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.elbmestd.getName(),
					PdmBOMDifferences.Row.META.elbme.getName());
			hasDifference = true;
		}
		if (productionListRow1.getElemQty().compareTo(productionListRow2.getElemQty()) != 0) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.anzahlstd.getName(),
					PdmBOMDifferences.Row.META.anzahl.getName());
			hasDifference = true;
		}
		if (productionListRow1.getCountUnit() != productionListRow2.getCountUnit()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.mellestd.getName(),
					PdmBOMDifferences.Row.META.melle.getName());
			hasDifference = true;
		}
		if (productionListRow1.getUse().compareTo(productionListRow2.getUse()) != 0) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.nutzenstd.getName(),
					PdmBOMDifferences.Row.META.nutzen.getName());
			hasDifference = true;
		}
		if (productionListRow1.getProvMatTransition() != productionListRow2.getProvMatTransition()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.bustd.getName(),
					PdmBOMDifferences.Row.META.bu.getName());
			hasDifference = true;
		}
		if (productionListRow1.getWorkCenter() != productionListRow2.getWorkCenter()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.mgrstd.getName(),
					PdmBOMDifferences.Row.META.mgr.getName());
			hasDifference = true;
		}
		if (productionListRow1.getWageGrp() != productionListRow2.getWageGrp()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.lgrstd.getName(),
					PdmBOMDifferences.Row.META.lgr.getName());
			hasDifference = true;
		}
		// Änderung Romaco
		if (productionListRow1.getYbelast() != productionListRow2.getYbelast()) {
			setFieldsColor(newTableRow, PdmBOMDifferences.Row.META.ybelaststd.getName(),
					PdmBOMDifferences.Row.META.ybelast.getName());
			hasDifference = true;
		}
		return hasDifference;
	}

	private PdmBOMDifferences.Row insertTableRow(final PdmBOMDifferences pdmbomdiff,
			final ProductionList.Row productionListRow1, final ProductionList.Row productionListRow2,
			Boolean bom1leer) {

		final PdmBOMDifferences.Row row;
		if (productionListRow1 != null && !bom1leer) {
			row = pdmbomdiff.table().appendRow();
		} else {
			row = pdmbomdiff.table().insertRow(this.indexInsert);
			this.indexInsert++;
		}
		if (productionListRow1 != null) {

			row.setStdpdmpos(productionListRow1.getPdmPosNo());
			row.setStdpdmzid(productionListRow1.getPdmRowId());
			row.setFlzidstd(productionListRow1.getString(ProductionList.Row.META.rowId.getName()));
			row.setVposstd(productionListRow1.getItemNo());
			row.setElexstd(productionListRow1.getProdListElem());
			row.setLgestd(productionListRow1.getSetupTime());
			if (productionListRow1.getSetupTimeUnit() != null) {
				row.setEllmestd(productionListRow1.getSetupTimeUnit());
			}
			row.setBreitestd(productionListRow1.getTimeLimUnit());
			if (productionListRow1.getTimeUnit() != null) {
				row.setElbmestd(productionListRow1.getTimeUnit());
			}
			row.setAnzahlstd(productionListRow1.getElemQty());
			if (productionListRow1.getCountUnit() != null) {
				row.setMellestd(productionListRow1.getCountUnit());
			}
			row.setNutzenstd(productionListRow1.getUse());
			row.setBustd(productionListRow1.getProvMatTransition());
			row.setMgrstd(productionListRow1.getWorkCenter());
			row.setLgrstd(productionListRow1.getWageGrp());

			if (productionListRow1.getProdListElem() instanceof Product) {

				Product product = (Product) productionListRow1.getProdListElem();
				row.setElnamestd(product.getDescrOperLang());

			} else if (productionListRow1.getProdListElem() instanceof MeansOfProduction) {

				MeansOfProduction meansOfProduction = (MeansOfProduction) productionListRow1.getProdListElem();
				row.setElnamestd(meansOfProduction.getDescrOperLang());

			} else if (productionListRow1.getProdListElem() instanceof Service) {

				Service service = (Service) productionListRow1.getProdListElem();
				row.setElnamestd(service.getDescrOperLang());

			} else if (productionListRow1.getProdListElem() instanceof Operation) {

				Operation operation = (Operation) productionListRow1.getProdListElem();
				row.setElnamestd(operation.getDescrOperLang());
			}
			row.setFilterstd(productionListRow1.getFilter());
			row.setFiltervglstd(productionListRow1.getFilterComparVal());
			row.setFiltervgl2std(productionListRow1.getFilterComparVal2());
			row.setPtextstd(productionListRow1.getItemText());
			row.setPverluststd(productionListRow1.getScrapPercSched());
			row.setVkpverluststd(productionListRow1.getScrapPercCost());
			row.setAmgestd(productionListRow1.getStartUpQty());
			row.setVarstd(productionListRow1.getVar());
			row.setPzeitstd(productionListRow1.getBufferTime());
			if (productionListRow1.getBufferTimeUnit() != null) {
				row.setPzeitlestd(productionListRow1.getBufferTimeUnit());
			}
			row.setTlzeitstd(productionListRow1.getTranspWaitTime());
			if (productionListRow1.getTranspWaitTimeUnit() != null) {
				row.setTlzeitlestd(productionListRow1.getTranspWaitTimeUnit());
			}
			row.setLfbeiststd(productionListRow1.getMatProvSubcont());
			row.setMzrsekstd(productionListRow1.getSetupTimeUnitSec());
			if (productionListRow1.getDisplayedSetupTimeUnit() != null) {
				row.setMzrstd(productionListRow1.getDisplayedSetupTimeUnit());
			}
			row.setMzesekstd(productionListRow1.getTimeLimUnitSec());
			if (productionListRow1.getDisplayedTimeLimUnit() != null) {
				row.setMzestd(productionListRow1.getDisplayedTimeLimUnit());
			}
			row.setTersatzstd(productionListRow1.getSparePartTab());
			row.setTverschltstd(productionListRow1.getWearPartTab());
			row.setTnwpflichtstd(productionListRow1.getDocObligationTab());
			row.setTserarchivstd(productionListRow1.getServiceArchiveTab());
			// Änderung Romaco
			row.setYbelaststd(productionListRow1.getYbelast());

		}
		if (productionListRow2 != null) {

			row.setPdmpos(productionListRow2.getPdmPosNo());
			row.setPdmzid(productionListRow2.getPdmRowId());
			row.setFlzid(productionListRow2.getString(ProductionList.Row.META.rowId.getName()));
			row.setVpos(productionListRow2.getItemNo());
			row.setElex(productionListRow2.getProdListElem());
			row.setLge(productionListRow2.getSetupTime());
			if (productionListRow2.getSetupTimeUnit() != null) {
				row.setEllme(productionListRow2.getSetupTimeUnit());
			}
			row.setBreite(productionListRow2.getTimeLimUnit());
			if (productionListRow2.getTimeUnit() != null) {
				row.setElbme(productionListRow2.getTimeUnit());
			}
			row.setAnzahl(productionListRow2.getElemQty());
			if (productionListRow2.getCountUnit() != null) {
				row.setMelle(productionListRow2.getCountUnit());
			}
			row.setNutzenstd(productionListRow2.getUse());
			row.setBu(productionListRow2.getProvMatTransition());
			row.setMgr(productionListRow2.getWorkCenter());
			row.setLgr(productionListRow2.getWageGrp());

			if (productionListRow2.getProdListElem() instanceof Product) {

				Product product = (Product) productionListRow2.getProdListElem();
				row.setElname(product.getDescrOperLang());

			} else if (productionListRow2.getProdListElem() instanceof MeansOfProduction) {

				MeansOfProduction meansOfProduction = (MeansOfProduction) productionListRow2.getProdListElem();
				row.setElname(meansOfProduction.getDescrOperLang());

			} else if (productionListRow2.getProdListElem() instanceof Service) {

				Service service = (Service) productionListRow2.getProdListElem();
				row.setElname(service.getDescrOperLang());

			} else if (productionListRow2.getProdListElem() instanceof Operation) {

				Operation operation = (Operation) productionListRow2.getProdListElem();
				row.setElname(operation.getDescrOperLang());
			}
			row.setFilter(productionListRow2.getFilter());
			row.setFiltervgl(productionListRow2.getFilterComparVal());
			row.setFiltervgl2(productionListRow2.getFilterComparVal2());
			row.setPtext(productionListRow2.getItemText());
			row.setPverlust(productionListRow2.getScrapPercSched());
			row.setVkpverlust(productionListRow2.getScrapPercCost());
			row.setAmge(productionListRow2.getStartUpQty());
			row.setVar(productionListRow2.getVar());
			row.setPzeit(productionListRow2.getBufferTime());
			if (productionListRow2.getBufferTimeUnit() != null) {
				row.setPzeitle(productionListRow2.getBufferTimeUnit());
			}
			row.setTlzeit(productionListRow2.getTranspWaitTime());
			if (productionListRow2.getTranspWaitTimeUnit() != null) {
				row.setTlzeitle(productionListRow2.getTranspWaitTimeUnit());
			}
			row.setLfbeist(productionListRow2.getMatProvSubcont());
			row.setMzrsek(productionListRow2.getSetupTimeUnitSec());
			if (productionListRow2.getDisplayedSetupTimeUnit() != null) {
				row.setMzr(productionListRow2.getDisplayedSetupTimeUnit());
			}
			row.setMzesek(productionListRow2.getTimeLimUnitSec());
			if (productionListRow2.getDisplayedTimeLimUnit() != null) {
				row.setMze(productionListRow2.getDisplayedTimeLimUnit());
			}
			row.setTersatz(productionListRow2.getSparePartTab());
			row.setTverschlt(productionListRow2.getWearPartTab());
			row.setTnwpflicht(productionListRow2.getDocObligationTab());
			row.setTserarchiv(productionListRow2.getServiceArchiveTab());
			// Änderung Romaco
			row.setYbelast(productionListRow2.getYbelast());

		}
		return row;
	}

	private void setBOMFieldsColor(final PdmBOMDifferences.Row row, String[] bomFields, String color) {

		for (String fieldName : bomFields) {
			if (color == null)
				resetBOMFieldColor(row, fieldName);
			else
				setBOMFieldColor(row, fieldName, color);
		}
	}

	private void setBOMFieldColor(final PdmBOMDifferences.Row row, String fieldName, String color) {

		FO.farbe(" -HINTERGRUND \"" + color + "\" " + fieldName + " " + row.getRowNo());
	}

	private void setFieldsColor(final PdmBOMDifferences.Row row, String fieldName1, String fieldName2) {

		setBOMFieldColor(row, fieldName1, colorBOM1Diff);
		setBOMFieldColor(row, fieldName2, colorBOM2Diff);
	}

	private void resetBOMFieldsColor(final PdmBOMDifferences.Row row, String[] bomFields) {

		setBOMFieldsColor(row, bomFields, null);
	}

	private void resetBOMFieldColor(final PdmBOMDifferences.Row row, String fieldName) {

		FO.farbe(" -HINTERGRUND -1 -1 -1 " + fieldName + " " + row.getRowNo());
	}
}
