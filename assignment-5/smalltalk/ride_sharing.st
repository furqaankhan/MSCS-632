Object subclass: Ride [
    | rideID pickupLocation dropoffLocation distance |

    <category: 'RideSharing'>

    Ride class >> id: anID pickup: aPickup dropoff: aDropoff distance: miles [
        ^self new
            initializeID: anID
            pickup: aPickup
            dropoff: aDropoff
            distance: miles
    ]

    Ride class >> formatDecimal: aNumber places: places [
        | multiplier scaledValue wholePart fractionalPart fractionalText |
        multiplier := 10 raisedToInteger: places.
        scaledValue := (aNumber * multiplier) rounded.
        wholePart := scaledValue // multiplier.
        fractionalPart := scaledValue \\ multiplier.
        fractionalText := fractionalPart printString.
        [fractionalText size < places] whileTrue: [
            fractionalText := '0', fractionalText
        ].
        ^wholePart printString, '.', fractionalText
    ]

    initializeID: anID pickup: aPickup dropoff: aDropoff distance: miles [
        (anID isEmpty or: [aPickup isEmpty or: [aDropoff isEmpty]])
            ifTrue: [self error: 'Ride identifiers and locations cannot be empty.'].
        miles <= 0
            ifTrue: [self error: 'Ride distance must be greater than zero.'].

        rideID := anID.
        pickupLocation := aPickup.
        dropoffLocation := aDropoff.
        distance := miles.
        ^self
    ]

    distance [
        ^distance
    ]

    fare [
        ^self subclassResponsibility
    ]

    rideType [
        ^self subclassResponsibility
    ]

    rideDetails [
        Transcript
            show: rideID;
            show: ' | ';
            show: self rideType;
            show: ' | ';
            show: pickupLocation;
            show: ' -> ';
            show: dropoffLocation;
            show: ' | ';
            show: (Ride formatDecimal: distance places: 2);
            show: ' miles | $';
            show: (Ride formatDecimal: self fare places: 2);
            nl
    ]
]

Ride subclass: StandardRide [
    <category: 'RideSharing'>

    fare [
        ^(325 / 100) + ((145 / 100) * self distance)
    ]

    rideType [
        ^'Standard'
    ]
]

Ride subclass: PremiumRide [
    <category: 'RideSharing'>

    fare [
        ^(750 / 100) + ((280 / 100) * self distance)
    ]

    rideType [
        ^'Premium'
    ]
]

Ride subclass: SharedRide [
    <category: 'RideSharing'>

    fare [
        ^5 max: (2 + self distance)
    ]

    rideType [
        ^'Shared'
    ]
]

Object subclass: Driver [
    | driverID name rating assignedRides |

    <category: 'RideSharing'>

    Driver class >> id: anID name: aName rating: aRating [
        ^self new initializeID: anID name: aName rating: aRating
    ]

    initializeID: anID name: aName rating: aRating [
        (anID isEmpty or: [aName isEmpty])
            ifTrue: [self error: 'Driver ID and name cannot be empty.'].
        (aRating < 0 or: [aRating > 5])
            ifTrue: [self error: 'Driver rating must be between 0 and 5.'].

        driverID := anID.
        name := aName.
        rating := aRating.
        assignedRides := OrderedCollection new.
        ^self
    ]

    addRide: aRide [
        aRide isNil ifTrue: [self error: 'Cannot assign an empty ride.'].
        assignedRides add: aRide
    ]

    getDriverInfo [
        Transcript
            show: 'Driver ';
            show: driverID;
            show: ': ';
            show: name;
            show: ' | Rating: ';
            show: (Ride formatDecimal: rating places: 1);
            show: '/5.0';
            nl;
            show: 'Completed rides: ';
            show: assignedRides size printString;
            nl.
        assignedRides do: [:ride | ride rideDetails]
    ]
]

Object subclass: Rider [
    | riderID name requestedRides |

    <category: 'RideSharing'>

    Rider class >> id: anID name: aName [
        ^self new initializeID: anID name: aName
    ]

    initializeID: anID name: aName [
        (anID isEmpty or: [aName isEmpty])
            ifTrue: [self error: 'Rider ID and name cannot be empty.'].

        riderID := anID.
        name := aName.
        requestedRides := OrderedCollection new.
        ^self
    ]

    requestRide: aRide [
        aRide isNil ifTrue: [self error: 'Cannot request an empty ride.'].
        requestedRides add: aRide
    ]

    viewRides [
        Transcript
            show: 'Rider ';
            show: riderID;
            show: ': ';
            show: name;
            nl;
            show: 'Requested rides: ';
            show: requestedRides size printString;
            nl.
        requestedRides do: [:ride | ride rideDetails]
    ]
]

| morningCommute airportTransfer campusTrip dinnerTrip rides totalFare driver rider |

morningCommute := StandardRide
    id: 'R-1001'
    pickup: 'Central Library'
    dropoff: 'Tech Park'
    distance: (48 / 10).
airportTransfer := PremiumRide
    id: 'R-1002'
    pickup: 'Riverside Hotel'
    dropoff: 'City Airport'
    distance: (1125 / 100).
campusTrip := SharedRide
    id: 'R-1003'
    pickup: 'North Campus'
    dropoff: 'Union Station'
    distance: (64 / 10).
dinnerTrip := PremiumRide
    id: 'R-1004'
    pickup: 'Art Museum'
    dropoff: 'Harbor Restaurant'
    distance: (25 / 10).

rides := OrderedCollection
    with: morningCommute
    with: airportTransfer
    with: campusTrip
    with: dinnerTrip.

Transcript
    show: 'RIDE SHARING SYSTEM - SMALLTALK';
    nl;
    show: '========================================';
    nl;
    show: 'All rides (polymorphic fare calculation)';
    nl.

totalFare := 0.
rides do: [:each |
    totalFare := totalFare + each fare.
    each rideDetails
].
Transcript
    show: 'Combined fare: $';
    show: (Ride formatDecimal: totalFare places: 2);
    nl;
    nl.

driver := Driver id: 'D-204' name: 'Maya Chen' rating: (49 / 10).
driver addRide: morningCommute.
driver addRide: airportTransfer.

Transcript
    show: 'Driver record';
    nl;
    show: '----------------------------------------';
    nl.
driver getDriverInfo.

rider := Rider id: 'U-731' name: 'Jordan Lee'.
rider requestRide: morningCommute.
rider requestRide: campusTrip.
rider requestRide: dinnerTrip.

Transcript
    nl;
    show: 'Rider history';
    nl;
    show: '----------------------------------------';
    nl.
rider viewRides.
