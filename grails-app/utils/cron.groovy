import subscriberpoc.Agency

/**
 * Created by paulk on 13/03/15.
 *
 * Cron job setup to run over each site and agency, and return a list of results based on filters set in the application
 *
 */


def agencyList = Agency.list();

print agencyList