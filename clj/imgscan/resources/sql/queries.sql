-- :name create-image! :! :n
INSERT INTO imgscan_image
  (imgfile, detect, scanned)
VALUES
  (:imgfile, :detect, :scanned)


-- :name get-images :? :*
SELECT * FROM imgscan_image
