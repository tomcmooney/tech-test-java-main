# System Loco Server Tech Test

Coding exercise designed to replicate adding a new "list devices" endpoint the System Loco _LocoAware_ server.

## Objective

This technical test is designed to replicate a realistic work scenario. It’s a chance for you to demonstrate your development talents. The idea is you should be able to complete this in an hour or so. We are not testing how much time you can put in so do not feel pressurised to provide a “complete” production solution. 

If you have any questions, please email them to m.johnson@systemloco.com, just like in a real environment, asking questions is encouraged. 
Although this is a contrived and time restricted task please treat this as you would for a production system. If you would do something for a production system please include examples of such things.

## Task

This repository contains a template server application that connects to a MongoDB instance. Add a new endpoint that allows our portal to show a list of devices.  

You can see examples of some of our platform designs [here](https://www.figma.com/design/ZI9psxbvmVJrwUcJdMIgyT/Frontend-Design?node-id=0-1&t=xwIKlm0bTVKNzVer-1). 

Once the server is running you’ll be able verify your connection and retrieve details of a single device by going to: [http://localhost:8080/api/device/72300000000000001](http://localhost:8080/api/device/123123)

## Notes

 - The APIs in this test do not require authentication but in our production system they obviously do.
 - In our portal, we default to showing devices ordered on "last reported" time with the most recent first.
 - Device documents are stored in the `device` collection. To help simplify the task, the collection used for this task contains a simplified representation of the device document.
 - Devices with the `deactivated: true` flag should ideally not be returned by the APIs.
 - The `device` collection has the following indexes:
   ```js
    [
        {
            key: {
                _id: 1
            },
            name: "_id_"
        },
        {
            key: {
                device: 1
            },
            name: "device_1",
            unique: true
        },
        {
            key: {
                lastReported: -1,
                device: -1
            },
            name: "lastReported_-1_device_-1",
            partialFilterExpression: {
                deactivated: false
            }
        },
        {
            key: {
                name: -1,
                device: -1
            },
            name: "name_-1_device_-1",
            partialFilterExpression: {
                deactivated: false
            }
        }
    ]
    ```

## Deliverable  

Please email a copy of the project source code and any build instructions to m.johnson@systemloco.com or provide a link to a github repository.