package pacote.mestrado.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class DateUtil {

    private static LinkedList<Integer> fimDeSemana;

    public static int getDiferencaEmDias(Date dateBase, Date dataTermino) {
	long dt = (dataTermino.getTime() - dateBase.getTime()) + 3600000;
	int dias = (int) (dt / 86400000L);
	return dias;
    }

    public static int getDiferencaEmDiasUteis(Date dateBase, Date dataTermino) {
	int retorno = 0;
	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(dateBase);

	List<Integer> fimDeSemana = new LinkedList<Integer>();
	fimDeSemana.add(Calendar.SATURDAY);
	fimDeSemana.add(Calendar.SUNDAY);

	for (int i = 0; i < getDiferencaEmDias(dateBase, dataTermino); i++) {
	    if (!fimDeSemana.contains(cal.get(Calendar.DAY_OF_WEEK))) {
		retorno++;
	    }
	    cal.add(Calendar.DAY_OF_MONTH, 1);
	}

	return retorno;
    }

    public static Date adicionaDias(Date dataInicio, int nDias) {
	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(dataInicio);
	cal.add(Calendar.DAY_OF_MONTH, nDias);

	return cal.getTime();
    }

    public static Date adicionaDiasUteis(Date dataInicio, int nDias) {
	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(dataInicio);

	defineFinaisSemana();

	while (nDias > 0) {
	    cal.add(Calendar.DAY_OF_MONTH, 1);
	    if (!fimDeSemana.contains(cal.get(Calendar.DAY_OF_WEEK))) {
		nDias--;
	    }
	}

	return cal.getTime();
    }

    private static void defineFinaisSemana() {
	fimDeSemana = new LinkedList<Integer>();
	fimDeSemana.add(Calendar.SATURDAY);
	fimDeSemana.add(Calendar.SUNDAY);
    }

    public static Date subtraiDiasUteis(Date dataInicio, int nDias) {
	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(dataInicio);

	defineFinaisSemana();

	while (nDias > 0) {
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    if (!fimDeSemana.contains(cal.get(Calendar.DAY_OF_WEEK))) {
		nDias--;
	    }
	}

	return cal.getTime();
    }

}
