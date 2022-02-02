from django.db import models


def imgpath(model, filename):
    return F'imgscan/{model.user.id}/{filename}'


class Image(models.Model):
    dbobjects = models.Manager()

    id = models.BigAutoField(primary_key=True)
    imgfile = models.FileField(upload_to=imgpath)
    detect = models.BooleanField(default=True)
    
    # The spec calls for a field named 'objects' but this collides
    # with a Python field of the same name. We'll rename the field in
    # the serializer
    objects = models.ForeignKey(
        'ImgObject', on_delete=models.CASCADE, null=True)


class ImgObject(models.Model):
    label = models.CharField(primary_key=True, max_length=256)

    def __str__(self):
        return self.label
