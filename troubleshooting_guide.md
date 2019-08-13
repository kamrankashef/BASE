Exception:
```bash
java.lang.RuntimeException: Model not found for prefix 'Event' at index 0
```

Meaning:
You are trying to adjoin models, but the Model map is missing a referenced Model.
You may have seen an error like:
`Model at index 0 with name Event is null`.

As an example in the `TestHospitalEvents` example, if

```java
// Commenting out this line would cause the exception
eventSubModels.put("Event", models.get("Event"));

final AbstractModel adjoinedModel
        = adjoinModelUtil.adjoinModels(
        modelNames, prefixes, "Event", eventSubModels, customAttribute);
```


