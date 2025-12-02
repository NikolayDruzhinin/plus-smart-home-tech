CREATE ROLE analyzer_user      LOGIN PASSWORD 'password';
CREATE ROLE commerce_cart_user LOGIN PASSWORD 'password';
CREATE ROLE commerce_store_user LOGIN PASSWORD 'password';
CREATE ROLE commerce_wh_user   LOGIN PASSWORD 'password';

CREATE DATABASE "smart-home-telemetry-analyzer";
CREATE DATABASE "smart-home-commerce-cart";
CREATE DATABASE "smart-home-commerce-store";
CREATE DATABASE "smart-home-commerce-warehouse";

ALTER DATABASE "smart-home-telemetry-analyzer" OWNER TO analyzer_user;
ALTER DATABASE "smart-home-commerce-cart"      OWNER TO commerce_cart_user;
ALTER DATABASE "smart-home-commerce-store"     OWNER TO commerce_store_user;
ALTER DATABASE "smart-home-commerce-warehouse" OWNER TO commerce_wh_user;
