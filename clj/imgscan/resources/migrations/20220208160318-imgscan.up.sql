CREATE TABLE imgscan_image (
  id integer NOT NULL PRIMARY KEY AUTO_INCREMENT,
  imgfile varchar(100) NOT NULL UNIQUE,
  detect bool NOT NULL,
  scanned datetime NULL);

CREATE TABLE imgscan_imgobject (
  id integer NOT NULL PRIMARY KEY AUTO_INCREMENT,
  label varchar(256) NOT NULL UNIQUE);

CREATE TABLE imgscan_image_objects (
  id integer NOT NULL PRIMARY KEY AUTO_INCREMENT,
  image_id integer NULL,
  imgobject_id integer NULL,
  FOREIGN KEY (image_id) REFERENCES
    imgscan_image (id),
  FOREIGN KEY (imgobject_id) REFERENCES
    imgscan_imgobject (id));

-- CREATE UNIQUE INDEX
--   imgscan_image_objects_image_id_imgobject_id
--     ON imgscan_image_objects (image_id, imgobject_id);
        
-- CREATE INDEX imgscan_image_objects_image_id
--   ON imgscan_image_objects (image_id);
    
-- CREATE INDEX imgscan_image_objects_imgobject_id
--   ON imgscan_image_objects (imgobject_id);
