-- :name create-image! :i! :*
INSERT INTO imgscan_image
  (imgfile, detect)
VALUES
  (:imgfile, :detect)


-- :name get-images :? :*
SELECT * FROM imgscan_image
