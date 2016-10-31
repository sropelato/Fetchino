[![Build Status](https://travis-ci.org/barkbeetle/Fetchino.svg?branch=master)](https://travis-ci.org/barkbeetle/Fetchino)

Fetchino is a lightweight web scraper, helping you get structured information from websites. It uses configuration files to describe how the data you're looking for is retrieved and makes it super-easy access the data in an API-like way.

### Example: Who played in the first Lord of the Rings movie?

The following configuration will tell Fetchino to open the imdb.com page for "The Lord of the Rings: The Fellowship of the Ring" and find all actors.

##### Config

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <config>
        <data>
            <list name="actors" type="string" />
        </data>
        <workflow>
            <request url="http://www.imdb.com/title/tt0120737/fullcredits" />
            <addToList list="actors" path="//table[@class='cast_list']/tbody/tr[@class]/td[2]/a" />
        </workflow>
    </config>

The names of the actors are stored in a list named "actors" which can be accessed as shown below:

##### Java Code

    Fetchino fetchino = Fetchino.fromConfig("./LordOfTheRingsActors.xml"));
    fetchino.fetch();
    fetchino.getContext().getList("actors").forEach(System.out::println);
