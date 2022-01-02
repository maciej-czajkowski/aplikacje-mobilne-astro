package czajkowski.maciej.astro;

public class ResourceHandler {

    public static int getIconResource(String icon) {
        switch (icon) {
            case "01d":
                return R.drawable.i01d;
            case "01n":
                return R.drawable.i01n;
            case "02d":
                return R.drawable.i02d;
            case "02n":
                return R.drawable.i02n;
            case "03d":
                return R.drawable.i03d;
            case "03n":
                return R.drawable.i03n;
            case "04d":
                return R.drawable.i04d;
            case "04n":
                return R.drawable.i04n;
            case "09d":
                return R.drawable.i09d;
            case "09n":
                return R.drawable.i09n;
            case "10d":
                return R.drawable.i10d;
            case "10n":
                return R.drawable.i10n;
            case "11d":
                return R.drawable.i11d;
            case "11n":
                return R.drawable.i11n;
            case "13d":
                return R.drawable.i13d;
            case "13n":
                return R.drawable.i13n;
            case "i50d":
                return R.drawable.i50d;
            case "i50n":
            default:
                return R.drawable.i50n;
        }
    }

    public static int getDefaultIcon() {
        return R.drawable.i50n;
    }
}
