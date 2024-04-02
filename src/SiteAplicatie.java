public class SiteAplicatie {
    // clasa serviciu de tip singletone
    private static SiteAplicatie siteAplicatie;
    private SiteAplicatie() {

    }

    public static SiteAplicatie getSiteAplicatie() {
        if (siteAplicatie == null)
            siteAplicatie = new SiteAplicatie();
        return siteAplicatie;
    }
}
