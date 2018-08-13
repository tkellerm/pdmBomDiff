package de.abas.erp.infosystem.owpdm.pdmbomdiff;

import java.util.Comparator;

public class PdmBomDiffRowComparator implements Comparator<PdmBOMDifferencesRow> {

	@Override
	public int compare(PdmBOMDifferencesRow arg0, PdmBOMDifferencesRow arg1) {

		if (arg0 != null && arg1 != null) {
			return arg0.getSortpdmpos() - arg1.getSortpdmpos();

		} else if (arg0 != null && arg1 == null) {
			return 1;
		} else {
			return -1;
		}

	}

}
