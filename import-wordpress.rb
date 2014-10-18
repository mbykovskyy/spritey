require "jekyll-import"

JekyllImport::Importers::WordPress.run({
  "dbname"   => "bykovskyy_com_max_db",
  "user"     => "max_db",
  "password" => "jkUSpdjs9",
  "host"     => "208.97.160.38",
  "socket"   => "",
  "table_prefix"   => "spritepacker_",
  "clean_entities" => true,
  "comments"       => false,
  "categories"     => true,
  "tags"           => true,
  "more_excerpt"   => true,
  "more_anchor"    => true,
  "status"         => ["publish"]
})