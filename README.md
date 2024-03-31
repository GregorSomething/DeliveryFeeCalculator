# Delivery fee calculator
Java Programming Trial Task
## Endpoints
`/api/delivery_fee` accepts POST requests:
```json
{
  "city": "<city>",
  "vehicle": "<vehicle>"
}
```
and 
```json
{
  "city": "<city>",
  "vehicle": "<vehicle>",
  "atTimestamp": <unix_timestamp>
}
```
atTimestamp is optional, but if it is in future or no data is about weather at that time, then error is give.  
Errors are in format:
```json
{
  "status": "<STATUS>",
  "message": "<message>"
}
```
Exsample of a response to a request looks like:
```json
{
  "deliveryFee": 4.0
}
```

## Other
Cron job timing can be edited at application.properties, key: `weather.update.cron`