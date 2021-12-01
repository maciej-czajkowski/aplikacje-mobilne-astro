package czajkowski.maciej.astro;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;

public class AstroCalculatorFactory {

    public static AstroCalculator getCurrentAstroCalculator(double latitude, double longitude) {
        AstroDateTime astroDateTime = new AstroDateTime(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND),
                Calendar.getInstance().get(Calendar.ZONE_OFFSET),
                true);

        return new AstroCalculator(astroDateTime, new AstroCalculator.Location(latitude, longitude));
    }
}
