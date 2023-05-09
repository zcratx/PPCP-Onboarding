
<script>

    alert("the script is in
    var myDeviceData;

    var venmoButton = document.getElementById('venmo-button');

        // Create a client.
        braintree.client.create({

                  authorization:'sandbox_fwmgqrp9_6nxmgsydbph8hnbr',
                  /**
                  * THIS TOGGLE WILL PREVENT APP BROSWERS TO BE OPENED WHEN VENMO PAYMENT IS
                  * SHARED ACROSS APPS LIKE INSTAGRAM, TWITTER
                  */
                  allowWebviews: false

            }, function (clientErr, clientInstance) {

                // Stop if there was a problem creating the client.
                // This could happen if there is a network error or if the authorization
                // is invalid.
                if (clientErr) {
                    console.error('Error creating client:', clientErr);
                    return;
                }

                // this is to load the data collector
                  braintree.dataCollector.create({
                    client: clientInstance,
                    paypal: true
                }, function (err, dataCollectorInstance) {
                     if (err) {
                     // Handle error
                     dataCollectorInstance.teardown();
                     return;
                    }
                    // At this point, you should access the
                    //dataCollectorInstance.deviceData value and provide it
                    // to your server, e.g. by injecting it into your form as hidden input
                    myDeviceData = dataCollectorInstance.deviceData;
                    console.log("The device data is "+ JSON.stringify(myDeviceData));
                 });

                // Create a Venmo component.
                braintree.venmo.create({
                    client: clientInstance,
                    profileId: '1953896702662410263',
                    allowDesktop:true,
                    allowDesktopWebLogin: true,

                  // Add allowNewBrowserTab: false if your checkout page does not support
                  // relaunching in a new tab when returning from the Venmo app. This can
                  // be omitted otherwise.
                  // allowNewBrowserTab: false

                  /**
                  * This is to prevent auto return and forcing Manual return
                  * Passing allowNewBrowserTab as false when calling Venmo Create make the non-native browsers to
                  * return false. When the isBrowserSupported return false, then set requireManualReturn to TRUE
                  * This will make a shortcut appear on the top left of the screen which should make it easy
                  * for the customer to return to the broswer or they can manually app switch
                  */

                  requireManualReturn: !braintree.venmo.isBrowserSupported({
                              allowNewBrowserTab: false
                   }),


                  /**
                  * This indicates if the payment is off a subscription or one time payment
                  * will be multi_use for subscriptions
                  */

                  paymentMethodUsage: "multi_use",
                  mobileWebFallback: true


                }, function (venmoErr, venmoInstance) {

                    // Stop if there was a problem creating Venmo Instance.
                    // This could happen if there was a network error or if it's incorrectly
                    // configured.
                    if (venmoErr) {
                    console.error('Error creating Venmo:', venmoErr);
                    return;
                    }

                  // Verify browser support before proceeding.
                  if (!venmoInstance.isBrowserSupported()) {
                    console.log('Browser does not support Venmo');
                    return;
                  }

                  // this is to display venmo
                  displayVenmoButton(venmoInstance);

                  // Check if tokenization results already exist. This occurs when your
                  // checkout page is relaunched in a new tab. This step can be omitted
                  // if allowNewBrowserTab is false.
                  if (venmoInstance.hasTokenizationResult()) {
                    venmoInstance.tokenize(function (tokenizeErr, payload) {
                      if (err) {
                        handleVenmoError(tokenizeErr);
                      } else {
                        handleVenmoSuccess(payload);
                      }
                    });
                    return;
                  }
          });
        });

        function displayVenmoButton(venmoInstance) {
          // Assumes that venmoButton is initially display: none.
          console.log("About to display the venmo button");

          venmoButton.style.display = 'block';

          venmoButton.addEventListener('click', function () {
            venmoButton.disabled = true;

            venmoInstance.tokenize(function (tokenizeErr, payload) {
              venmoButton.removeAttribute('disabled');

               if (tokenizeErr) {
                handleVenmoError(tokenizeErr);
              } else {
                handleVenmoSuccess(payload);
              }

            });
          });
        }

        function handleVenmoError(err) {
           if (err.code === 'VENMO_CANCELED') {
            console.log('App is not available or user aborted payment flow');
          } else if (err.code === 'VENMO_APP_CANCELED') {
            console.log('User canceled payment flow');
          } else {
            console.error('An error occurred:', err.message);
          }
        }

        function handleVenmoSuccess(payload) {
          // Send payload.nonce to your server.
          console.log('Got a payment method nonce:', payload.nonce);
          console.log(payload);
          console.log(JSON.stringify(payload.details));
          // Display the Venmo username in your checkout UI.
          console.log('Venmo user:', payload.details.username);
          console.log('First Name: ',payload.details.payerInfo.firstName);
          console.log('Last Name: ',payload.details.payerInfo.lastName);
          console.log('Email: ',payload.details.payerInfo.email);
          console.log('Phone Number: ',payload.details.payerInfo.phoneNumber);
        }



    </script>