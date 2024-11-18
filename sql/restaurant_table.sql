CREATE DATABASE restaurantdb;

-- RESTAURANT TABLE

DROP TABLE IF EXISTS public.restaurant CASCADE;
DROP SEQUENCE IF EXISTS public.restaurant_id_seq;

CREATE SEQUENCE public.restaurant_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.restaurant_id_seq OWNER TO postgres;

CREATE TABLE public.restaurant (
    id integer NOT NULL DEFAULT nextval('restaurant_id_seq'::regclass),
    name text,
    address text,
    opening_hours text,
    closing_hours text,
    rating real,
    owner_id integer NOT NULL,
    CONSTRAINT restaurant_id_pk PRIMARY KEY (id)
);

ALTER TABLE public.restaurant OWNER to postgres;  

INSERT INTO public.restaurant (name, address, opening_hours, closing_hours, rating, owner_id) VALUES
    ('La Dolce Vita', '123 Main St, Cityville', '10:00', '22:00', 4.5, 3),
    ('Sushi Zen' , '456 Elm St, Cityville', '11:00', '23:00', 4.7, 6),
    ('Taco Loco', '789 Oak St, Cityville', '09:00', '21:00', 4.3, 6),
    ('The Curry House', '321 Maple St, Cityville', '12:00', '22:30', 4.6, 10),
    ('Burger Haven', '654 Pine St, Cityville', '08:00', '20:00', 4.2, 14),
    ('Pho Paradise', '987 Cedar St, Cityville', '10:00', '22:00', 4.4, 3),
    ('Bistro Paris', '159 Walnut St, Cityville', '07:00', '19:00', 4.8, 6),
    ('Pizzeria Italia', '753 Birch St, Cityville', '11:00', '23:00', 4.9, 10),
    ('Kebab King', '852 Chestnut St, Cityville', '10:00', '00:00', 4.5, 14),
    ('Green Garden', '951 Spruce St, Cityville', '08:30', '20:30', 4.3, 16);

-- MENU CATEGAORY TABLE

DROP TABLE IF EXISTS public.menu_category CASCADE;
DROP SEQUENCE IF EXISTS public.menu_category_id_seq;

CREATE SEQUENCE public.menu_category_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.menu_category_id_seq OWNER TO postgres;

CREATE TABLE public.menu_category (
    id integer NOT NULL DEFAULT nextval('menu_category_id_seq'::regclass),
    name text NOT NULL,
    CONSTRAINT menu_category_pkey PRIMARY KEY (id)
);

ALTER TABLE public.menu_category OWNER to postgres;

INSERT INTO public.menu_category (name) VALUES
    ('Appetizers & Starters'),
    ('Salads'),
    ('Soups'),
    ('Main Courses'),
    ('Sandwiches & Wraps'),
    ('Burgers'),
    ('Pasta & Noodles'),
    ('Pizza'),
    ('Rice & Biryani'),
    ('Desserts'),
    ('Beverages'),
    ('Seafood'),
    ('Vegetarian Dishes'),
    ('Vegan Dishes'),
    ('Gluten-Free Options'),
    ('Breakfast & Brunch'),
    ('Healthy & Low-Calorie'),
    ('Sides & Extras'),
    ('Kids Meals'),
    ('Combos & Family Packs');

-- CUISINE TABLE

DROP TABLE IF EXISTS public.cuisine CASCADE;
DROP SEQUENCE IF EXISTS public.cuisine_id_seq;

CREATE SEQUENCE public.cuisine_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.cuisine_id_seq OWNER TO postgres;

CREATE TABLE public.cuisine (
    id integer NOT NULL DEFAULT nextval('cuisine_id_seq'::regclass),
    name text NOT NULL,
    CONSTRAINT cuisine_pkey PRIMARY KEY (id)
);

ALTER TABLE public.cuisine OWNER to postgres;

INSERT INTO public.cuisine (name) VALUES
    ('Italian'),
    ('French'),
    ('Chinese'),
    ('Japanese'),
    ('Mexican'),
    ('Indian'),
    ('Thai'),
    ('Greek'),
    ('Korean'),
    ('Spanish'),
    ('Lebanese'),
    ('Turkish'),
    ('Moroccan'),
    ('Vietnamese'),
    ('Brazilian'),
    ('Ethiopian'),
    ('Caribbean'),
    ('German'),
    ('Persian (Iranian)'),
    ('Indonesian'),
    ('Russian'),
    ('Argentinian'),
    ('Cuban'),
    ('Pakistani'),
    ('South African'),
    ('Jamaican'),
    ('Filipino'),
    ('Egyptian'),
    ('Syrian'),
    ('Malaysian'),
    ('Sri Lankan'),
    ('Portuguese'),
    ('Nepalese'),
    ('Bangladeshi'),
    ('Polish'),
    ('Belgian'),
    ('Hawaiian'),
    ('Afghan'),
    ('Hungarian'),
    ('Swiss'),
    ('British'),
    ('American (New American)'),
    ('Australian'),
    ('Uzbek'),
    ('Israeli'),
    ('Peruvian'),
    ('Colombian'),
    ('Saudi Arabian'),
    ('Singaporean'),
    ('Tibetan');

-- MENU TABLE

DROP TABLE IF EXISTS public.menu_item;
DROP SEQUENCE IF EXISTS public.menu_item_id_seq;

CREATE SEQUENCE public.menu_item_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.menu_item_id_seq OWNER TO postgres;

CREATE TABLE public.menu_item (
    id integer NOT NULL DEFAULT nextval('menu_item_id_seq'::regclass),
    restaurant_id integer NOT NULL,
    category_id integer NOT NULL,
    cuisine_id integer NOT NULL,
    name text,
    description text,
    price integer,
    available boolean,
    rating real,
    preparation_time integer,
    pure_veg boolean,
    CONSTRAINT menu_item_pkey PRIMARY KEY (id),
    CONSTRAINT menu_item_restaurant_id_fk FOREIGN KEY (restaurant_id) REFERENCES public.restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menu_item_category_id_fk FOREIGN KEY (category_id) REFERENCES public.menu_category (id),
    CONSTRAINT menu_item_cuisine_id_fk FOREIGN KEY (cuisine_id) REFERENCES public.cuisine (id)
);

ALTER TABLE public.menu_item OWNER to postgres;

INSERT INTO public.menu_item (restaurant_id, category_id, cuisine_id, name, description, price, available, rating, preparation_time, pure_veg) VALUES
    (1,1,1,'Margherita Pizza','Classic pizza with tomato sauce and fresh mozzarella',8.99,TRUE,4.5,15,TRUE),
    (1,2,2,'Pasta Carbonara','Creamy pasta with pancetta and parmesan',12.99,TRUE,4.7,20,FALSE),
    (1,3,3,'Vegetable Spring Rolls','Deep-fried rolls filled with mixed vegetables',6.99,TRUE,4.2,10,TRUE),
    (1,4,4,'Chicken Tikka Masala','Grilled chicken in creamy tomato sauce',13.99,TRUE,4.6,25,FALSE),
    (1,5,5,'Beef Burger','Juicy beef patty with lettuce  tomato and cheese',10.99,TRUE,4.4,15,FALSE),
    (1,6,6,'Pad Thai', 'Fried rice noodles with shrimp peanuts and lime',11.99,TRUE,4.5,15,FALSE),
    (1,7,7,'Chickpea Salad', 'Fresh salad with chickpeas cucumber and tahini dressing',7.99,TRUE,4.3,10,TRUE),
    (1,8,8,'Chocolate Lava Cake','Decadent chocolate cake with a molten center',5.99,TRUE,4.8,10,TRUE),
    (1,9,9,'Caesar Salad','Romaine lettuce with Caesar dressing and croutons',8.49,TRUE,4.3,5,TRUE),
    (1,10,10,'Sushi Platter','Assorted sushi rolls and sashimi',15.99,TRUE,4.9,30,FALSE),
    (2,11,11,'Mango Sticky Rice','Sweet sticky rice topped with mango and coconut milk',4.99,TRUE,4.7,10,TRUE),
    (2,12,12,'Falafel Wrap', 'Wrap with falafel hummus and veggies',7.49,TRUE,4.4,10,TRUE),
    (2,13,13,'Tandoori Chicken','Spicy marinated chicken cooked in a clay oven',14.99,TRUE,4.6,25,FALSE),
    (2,14,14,'Spaghetti Aglio e Olio', 'Pasta with garlic olive oil and chili flakes',9.99,TRUE,4.2,15,TRUE),
    (2,15,15,'Pork Ramen','Noodles in broth with pork and green onions',12.49,TRUE,4.4,20,FALSE),
    (2,16,16,'Lentil Soup','Hearty soup made with lentils and spices',6.49,TRUE,4.5,15,TRUE),
    (2,17,17,'Garlic Bread','Toasted bread with garlic butter',3.99,TRUE,4.3,5,TRUE),
    (2,18,18,'Coconut Curry','Vegetable curry cooked in coconut milk',10.99,TRUE,4.6,20,TRUE),
    (2,19,19,'Cheesecake','Smooth and creamy cheesecake',5.49,TRUE,4.8,10,TRUE),
    (2,20,20,'Vegetable Fried Rice','Stir-fried rice with mixed vegetables',7.99,TRUE,4.5,15,TRUE),
    (3,1,21,'Fish Tacos','Soft tacos with grilled fish and mango salsa',11.49,TRUE,4.5,15,FALSE),
    (3,2,22,'Quinoa Bowl','Quinoa with roasted vegetables and feta cheese',9.99,TRUE,4.6,15,TRUE),
    (3,3,23,'Spicy Tuna Tartare','Raw tuna with spicy sauce and avocado',12.99,TRUE,4.7,20,FALSE),
    (3,4,24,'Stuffed Peppers','Bell peppers stuffed with rice and beans',8.49,TRUE,4.3,25,TRUE),
    (3,5,25,'Ribeye Steak','Grilled ribeye steak with garlic butter',22.99,TRUE,4.8,30,FALSE),
    (3,6,26,'Vegetable Samosas','Deep-fried pastry filled with spiced potatoes',5.99,TRUE,4.2,10,TRUE),
    (3,7,27,'Pork Schnitzel','Breaded pork cutlet with lemon and capers',13.49,TRUE,4.4,25,FALSE),
    (3,8,28,'Pesto Pasta','Pasta with basil pesto and parmesan cheese',11.99,TRUE,4.5,20,TRUE),
    (3,9,29,'Hummus Platter','Hummus served with pita and vegetables',7.49,TRUE,4.6,10,TRUE),
    (3,10,30,'Lamb Gyro','Grilled lamb wrapped in pita with tzatziki',9.99,TRUE,4.5,15,FALSE),
    (4,11,31,'Banana Split','Sliced bananas with ice cream and toppings',4.99,TRUE,4.7,5,TRUE),
    (4,12,32,'Cilantro Lime Rice','Rice cooked with cilantro and lime',3.99,TRUE,4.2,5,TRUE),
    (4,13,33,'Chicken Quesadilla','Grilled chicken in a tortilla with cheese',9.49,TRUE,4.4,15,FALSE),
    (4,14,34,'Creamy Tomato Soup','Rich tomato soup with cream and basil',5.49,TRUE,4.3,10,TRUE),
    (4,15,35,'Vegetable Korma','Creamy vegetable curry with spices',10.99,TRUE,4.6,20,TRUE),
    (4,16,36,'Seafood Paella','Spanish rice dish with seafood and saffron',15.99,TRUE,4.8,30,FALSE),
    (4,17,37,'Peach Cobbler','Baked dessert with peaches and crumb topping',5.99,TRUE,4.5,10,TRUE),
    (4,18,38,'Chicken Fried Rice','Stir-fried rice with chicken and vegetables',8.99,TRUE,4.4,15,FALSE),
    (4,19,39,'Beef Stroganoff','Tender beef in creamy mushroom sauce',14.49,TRUE,4.6,25,FALSE),
    (4,20,40,'Greek Salad','Fresh salad with olives feta and cucumbers' ,7.99,TRUE,4.5,10,TRUE),
    (5,1,41,'Bruschetta','Grilled bread topped with tomatoes and basil',6.99,TRUE,4.6,10,TRUE),
    (5,2,42,'Beef Tacos','Soft tacos filled with seasoned beef',10.49,TRUE,4.5,15,FALSE),
    (5,3,43,'Shrimp Scampi','Shrimp cooked in garlic butter and white wine',14.99,TRUE,4.8,20,FALSE),
    (5,4,44,'Chili con Carne','Spicy beef and bean stew',9.99,TRUE,4.4,30,FALSE),
    (5,5,45,'Mediterranean Bowl','Quinoa veggies and tzatziki sauce',8.99,TRUE,4.7,15,TRUE),
    (5,6,46,'Lemon Sorbet','Frozen dessert with lemon flavor',3.49,TRUE,4.5,5,TRUE),
    (5,7,47,'Ratatouille','Stewed vegetables with herbs',10.99,TRUE,4.6,20,TRUE),
    (5,8,48,'BBQ Ribs','Tender ribs with BBQ sauce',15.99,TRUE,4.8,30,FALSE),
    (5,9,49,'Chocolates Mousse','Rich chocolate dessert',4.99,TRUE,4.7,10,TRUE),
    (5,10,50,'Crispy Tofu','Tofu stir-fried with vegetables',7.99,TRUE,4.5,15,TRUE),
    (6,11,1,'Spinach Dip','Cheesy spinach dip with tortilla chips',6.49,TRUE,4.4,10,TRUE),
    (6,12,2,'Peanut Noodles','Noodles tossed in a creamy peanut sauce',9.99,TRUE,4.5,15,TRUE),
    (6,13,3,'Chicken Alfredo','Pasta with creamy Alfredo sauce and chicken',12.99,TRUE,4.6,20,FALSE),
    (6,14,4,'Baklava','Layered pastry with nuts and honey',4.99,TRUE,4.8,10,TRUE),
    (6,15,5,'Quiche Lorraine','Savory pie with bacon and cheese',7.49,TRUE,4.4,25,FALSE),
    (6,16,6,'Caprese Salad','Tomato mozzarella and basil salad',8.49,TRUE,4.5,10,TRUE),
    (6,17,7,'Stuffed Zucchini','Zucchini filled with rice and vegetables',10.49,TRUE,4.7,20,TRUE),
    (6,18,8,'Tiramisu','Coffee-flavored Italian dessert',5.99,TRUE,4.9,10,TRUE),
    (6,19,9,'Gnocchi','Potato dumplings with marinara sauce',9.99,TRUE,4.4,20,TRUE),
    (6,20,10,'Pumpkin Soup','Seasonal soup made with pumpkin',5.49,TRUE,4.6,10,TRUE),
    (7,1,11,'Corn on the Cob','Grilled corn with butter',3.99,TRUE,4.5,5,TRUE),
    (7,2,12,'Seafood Chowder','Creamy chowder with shrimp and clams',9.99,TRUE,4.8,20,FALSE),
    (7,3,13,'Vegetable Stir Fry','Mixed vegetables stir-fried with soy sauce',7.99,TRUE,4.4,15,TRUE),
    (7,4,14,'Beef Wellington','Beef fillet wrapped in pastry',18.99,TRUE,4.7,35,FALSE),
    (7,5,15,'Coconut Shrimp','Shrimp coated in coconut and fried',12.49,TRUE,4.6,20,FALSE),
    (7,6,16,'Fried Green Tomatoes','Coated tomatoes fried and served with sauce',5.49,TRUE,4.4,10,TRUE),
    (7,7,17,'Mushroom Risotto','Rice cooked with mushrooms and cheese',11.99,TRUE,4.6,20,TRUE),
    (7,8,18,'Lemon Herb Chicken','Grilled chicken with lemon and herbs',14.99,TRUE,4.5,25,FALSE),
    (7,9,19,'Chocolate Chip Cookies','Baked cookies with chocolate chips',4.99,TRUE,4.8,10,TRUE),
    (7,10,20,'Baked Ziti','Pasta baked with cheese and marinara sauce',10.49,TRUE,4.6,20,TRUE),
    (8,11,21,'Tandoori Paneer','Cubes of paneer marinated in spices and grilled',12.99,TRUE,4.6,25,TRUE),
    (8,12,22,'Miso Soup','Japanese soup made with miso and seaweed',4.99,TRUE,4.5,5,TRUE),
    (8,13,23,'Lobster Roll','Lobster meat in a buttered roll',16.99,TRUE,4.7,25,FALSE),
    (8,14,24,'Ramen','Noodles in broth with various toppings',12.49,TRUE,4.5,20,FALSE),
    (8,15,25,'Beef Enchiladas','Tortillas filled with beef and cheese',10.99,TRUE,4.4,15,FALSE),
    (8,16,26,'Hibachi Chicken','Grilled chicken with vegetables',13.49,TRUE,4.6,20,FALSE),
    (8,17,27,'Potato Wedges','Seasoned potato wedges',3.49,TRUE,4.5,5,TRUE),
    (8,18,28,'Vegetable Biryani','Spicy rice dish with mixed vegetables',11.99,TRUE,4.7,20,TRUE),
    (8,19,29,'Clam Chowder','Rich soup with clams and potatoes',9.49,TRUE,4.6,20,FALSE),
    (8,20,30,'Mango Lassi','Sweet yogurt drink with mango',3.99,TRUE,4.8,5,TRUE),
    (9,1,31,'Tom Yum Soup','Spicy Thai soup with shrimp',9.99,TRUE,4.7,20,FALSE),
    (9,2,32,'Crab Cakes','Pan-fried crab cakes with remoulade',12.99,TRUE,4.6,25,FALSE),
    (9,3,33,'Vegetable Banh Mi','Vietnamese sandwich with pickled veggies',8.49,TRUE,4.5,10,TRUE),
    (9,4,34,'Choco Fudge Brownie','Rich brownie topped with chocolate sauce',4.99,TRUE,4.8,10,TRUE),
    (9,5,35,'Baked Eggplant','Stuffed eggplant with tomato sauce',9.99,TRUE,4.6,20,TRUE),
    (9,6,36,'Teriyaki Chicken','Grilled chicken with teriyaki sauce',13.49,TRUE,4.5,20,FALSE),
    (9,7,37,'Cucumber Salad','Fresh salad with cucumbers and vinegar',5.49,TRUE,4.5,5,TRUE),
    (9,8,38,'Seafood Risotto','Rice with seafood and saffron',15.99,TRUE,4.8,30,FALSE),
    (9,9,39,'Vegetable Lasagna','Layers of pasta with vegetables and cheese',11.49,TRUE,4.7,20,TRUE),
    (9,10,40,'Stuffed Mushrooms','Stuffed with cheese and herbs',6.99,TRUE,4.4,10,TRUE),
    (10,11,41,'Peking Duck','Crispy duck served with pancakes',20.99,TRUE,4.9,30,FALSE),
    (10,12,42,'Pork Fried Rice','Stir-fried rice with pork and vegetables',8.99,TRUE,4.5,15,FALSE),
    (10,13,43,'Cabbage Rolls','Grape leaves stuffed with rice and spices',9.99,TRUE,4.6,20,TRUE),
    (10,14,44,'Peanut Butter Pie','Rich dessert made with peanut butter',5.49,TRUE,4.7,10,TRUE),
    (10,15,45,'Fettuccine Alfredo','Pasta in creamy sauce with parmesan',11.99,TRUE,4.6,20,FALSE),
    (10,16,46,'Shrimp Fried Rice','Stir-fried rice with shrimp and vegetables',9.49,TRUE,4.5,15,FALSE),
    (10,17,47,'Olive Tapenade','Spread made from olives serve with bread',4.99,TRUE,4.4,5,TRUE),
    (10,18,48,'Vegetable Tempura','Battered and fried vegetables',7.99,TRUE,4.5,10,TRUE),
    (10,19,49,'Beef Pho','Noodle soup with beef and herbs',10.99,TRUE,4.8,20,FALSE),
    (10,20,50,'Chili Lime Corn Salad', 'Salad with corn chili and lime dressing',6.49,TRUE,4.6,10,TRUE);