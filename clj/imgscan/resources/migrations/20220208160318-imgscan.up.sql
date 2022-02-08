CREATE TABLE imgscan_image (
       id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
       imgfile varchar(100) NOT NULL UNIQUE,
       detect bool NOT NULL,
       scanned datetime NULL);

CREATE TABLE imgscan_imgobject (
       id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
       label varchar(256) NOT NULL UNIQUE);

CREATE TABLE imgscan_image_objects (
       id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
       image_id bigint NOT NULL REFERENCES
           imgscan_image (id) DEFERRABLE INITIALLY DEFERRED,
       imgobject_id bigint NOT NULL REFERENCES
           imgscan_imgobject (id) DEFERRABLE INITIALLY DEFERRED);

CREATE UNIQUE INDEX
    imgscan_image_objects_image_id_imgobject_id_4857e070_uniq
    ON imgscan_image_objects (image_id, imgobject_id);
        
CREATE INDEX imgscan_image_objects_image_id_b891c35f
    ON imgscan_image_objects (image_id);
    
CREATE INDEX imgscan_image_objects_imgobject_id_fe509086
    ON imgscan_image_objects (imgobject_id);
