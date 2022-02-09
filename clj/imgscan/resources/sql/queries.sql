-- :name create-image! :! :n
INSERT INTO imgscan_image
  (imgfile, detect, scanned)
VALUES
  (:imgfile, :detect)
