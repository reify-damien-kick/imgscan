-- :name create-image! :i! :*
INSERT INTO imgscan_image
  (imgfile, detect)
VALUES
  (:imgfile, :detect)

-- :name create-image-objects! :i! :*
INSERT INTO imgscan_image_objects
  (image_id, imgobject_id)
VALUES
  (:image-id, :imgobject-id)

-- :name create-imgobject! :i! :*
INSERT INTO imgscan_imgobject
  (label)
VALUES
  (:label)

-- :name get-images :? :*
SELECT id, imgfile, detect, scanned
FROM imgscan_image

-- :name get-imgobjects :? :*
SELECT id, label
FROM imgscan_imgobject

-- :name get-images-objects :? :*
SELECT id, image_id, imgobject_id
FROM imgscan_image_objects

-- :name update-image! :!
UPDATE imgscan_image
SET scanned = now()
WHERE id = :id
