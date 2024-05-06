# JDBC Lab

This project is meant to provide a simple example application for the students
of the Database course of the Computer Science and Engineering department of the
University of Bologna.

To better understand what classes we need to define we first have to think about
_what_ out application is going to look like and what data it's going to need.
We'll let our application's needs drive the implementation and definition of
the various data classes you'll see under `db_lab/data`.

Let's first have a look at what our application has to do:

- It needs to have a main page where all the previes of a product are displayed.
  - A product's preview consists of its name and associated tags.
- Upon clicking on a product preview, we want the product page to appear.
  - A product's page has the product's name, description and reports the list
    of materials it's composed of, serted by percentage.
  - A product's page also must have a button to get back to the home page.
- Our application also needs to gracefully handle possible failures that may
  happen when fetching products.

In order to display a preview page we are going to need a list of products'
previews. So we can define a class `ProductPreview` that represents just that:

```java
public final class ProductPreview {

  final int code;
  final String name;
  final List<Tag> tags;
  // ...
}

```

We can immediately see how a `ProductPreview` needs a list of tags, so we define
a class for that as well:

```java
public final class Tag {

  final String name;
}

```

Notice how our object model doesn't necessarily have to match exactly the model
of the database: there's no `ProductPreview` table in the schema we've defined.
But that's ok: we have to choose the model that makes the most sense for our
application instead of just copying the structure of the underlying database.
This means that we'll need a _translation layer_ to speak to our database and
build Java objects out of database tables.

To do that we can define various DAOs (Database Access Objects) whose role is to
communicate with the database and build the Java objects we actually need.
Let's have a look at what a DAO for `ProductPreview`s might look like.
All we need is a way to read all `ProductPreview`s so we can show them in the
application's homepage:

```java
import java.sql.Connection;

public final class ProductReview {

  // ...
  public final class DAO {

    public static List<ProductReview> list(Connection connection) {
      // ...
    }
  }
}

```

Notice how the `list` method takes the database connection as its input: we
don't want to create a new connection every time we want to run a SQL query,
instead we'll just create one and share it between all kind of calls.
Also this means that we can change the connection based on our needs: if we have
to test a DAO we can create a connection to a test database instead of the
production one and safely run all kind of queries.

Now let's jump into `list`'s implementation:

```java
private static final String LIST_QUERY =
  """
  select p.codProdotto, p.nome
  from   PRODOTTO p
  """;

public static List<ProductPreview> list(Connection connection) {
  // Here's the big picture idea: to create a list of previews, we first need
  // to list all the products with their respective name and code.
  // (Since we're just creating previews we don't need to fetch any other data
  // associated with the Product's entity).
  // For each product we also need to get a list of all its tags. To do that
  // we're going to need a second query (that is going to be in the Tag's
  // DAO).
}

```

First of all we need a list to accumulate the previews as we build them:

```java
public static List<ProductPreview> list(Connection connection) {
  var previews = new ArrayList<ProductPreview>();

  return previews;
}

```

We need to run `LIST_QUERY` and iterate over it's `ResultSet`:

```java
public static List<ProductPreview> list(Connection connection) {
  var previews = new ArrayList<ProductPreview>();

  try (
    var preparedStatement = DAOUtils.prepareStatement(connection, LIST_QUERY);
    var resultSet = statement.executeQuery();
  ) {
    // todo...
  } catch (SQLException e) {
    // We just wrap the checked SQLException in an unchecked one so that
    // it won't bubble up in all the method's signatures.
    throw new DAOException(e);
  }

  return previews;
}

```

Notice how both `statement` and `resultSet` are `Closable`: after being done
with each one we need to call their `close` method. A try-with-resources here
makes our code much cleaner and handles these resources properly for us.
Now we just need to iterate over the `resultSet`:

```java
public static List<ProductPreview> list(Connection connection) {
  var previews = new ArrayList<ProductPreview>();

  try (
    var preparedStatement = DAOUtils.prepareStatement(connection, LIST_QUERY);
    var resultSet = statement.executeQuery();
  ) {
    // `next` advances the resultSet to the next row and tells us wether we're
    // allowed to continue or not.
    while (resultSet.next()) {
      // To read columns from the curren row we will use the `getX` methods
      // of the result set.
      var code = resultSet.getInt("p.codProdotto");
      var name = resultSet.getString("p.nome");
      // Notice how the work of fetching a product's tags is carried out by
      // another DAO!
      var tags = Tag.DAO.listForProduct(connection, code);
      var preview = new ProductPreview(code, name, tags);
      previews.add(preview);
    }
  } catch (SQLException e) {
    throw new DAOException(e);
  }

  return previews;
}

```

As you can see we've discovered quite naturally the next method we're going to
need: `Tag.DAO.listForProduct`. Try implementing it on your own.
The implementation is going to be similar for the `Product`'s DAO: think about
the data your application needs and build a method to fetch that data and
package it in a plain old Java object.

## A small detour on the application's structure

This project has a pretty standard structure:

- We have a `data` package were we define all the classes needed by our
  application and the various DAOs to talk to the database.
- There's a `model` package where we define the interface of the model of the
  application. Here it's nothing more than a mere data container with a cache
  to hold onto the previews in order to not reload them each time we leave the
  preview page and later get back to it.
- There's a controller that defines all the possible ways in which the view can
  interact with the model. By reading its public methods we can get a complete
  idea of all the possible interactions that might take place, this is a really
  powerful property, especially when the application starts to grow.
- There's a view written in Swing that exposes some methods the controller can
  use to display specific scenes based on the state of the application.
- The `App` class works as the entry point: it establishes a connection to the
  database and creates the initial model (wich may be mocked), view and
  controller, wiring them all together.

When should I use an interface or just a class? You'll notice how we always use
classes directly except for the `Model`.
We only use an interface if it can provide us actual benefits: in the `Model`'s
case, by abstracting its behaviour behind an interface we can have a "mock"
model that doesn't need to go and read data from the database; this can allow us
to quickly test the app's GUI without having to waste time seeding a db and
setting up a connection.

All other concepts are modelled directly using classes, this has the benefit of
keeping our codebase small without burdening us with needles abstractions and
complexity.
