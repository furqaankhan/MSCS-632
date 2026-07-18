#include <iomanip>
#include <iostream>
#include <memory>
#include <stdexcept>
#include <string>
#include <utility>
#include <vector>

class Ride {
public:
    Ride(std::string rideID, std::string pickupLocation,
         std::string dropoffLocation, double distance)
        : rideID_(std::move(rideID)),
          pickupLocation_(std::move(pickupLocation)),
          dropoffLocation_(std::move(dropoffLocation)),
          distance_(distance) {
        if (rideID_.empty() || pickupLocation_.empty() || dropoffLocation_.empty()) {
            throw std::invalid_argument("Ride identifiers and locations cannot be empty.");
        }
        if (distance_ <= 0.0) {
            throw std::invalid_argument("Ride distance must be greater than zero.");
        }
    }

    virtual ~Ride() = default;

    virtual double fare() const = 0;
    virtual std::string rideType() const = 0;

    virtual void rideDetails(std::ostream& output = std::cout) const {
        output << rideID_ << " | " << rideType() << " | "
               << pickupLocation_ << " -> " << dropoffLocation_
               << " | " << std::fixed << std::setprecision(2) << distance_
               << " miles | $" << fare() << '\n';
    }

protected:
    double distance() const {
        return distance_;
    }

private:
    std::string rideID_;
    std::string pickupLocation_;
    std::string dropoffLocation_;
    double distance_;
};

class StandardRide final : public Ride {
public:
    using Ride::Ride;

    double fare() const override {
        return 3.25 + (1.45 * distance());
    }

    std::string rideType() const override {
        return "Standard";
    }
};

class PremiumRide final : public Ride {
public:
    using Ride::Ride;

    double fare() const override {
        return 7.50 + (2.80 * distance());
    }

    std::string rideType() const override {
        return "Premium";
    }
};

class SharedRide final : public Ride {
public:
    using Ride::Ride;

    double fare() const override {
        const double calculatedFare = 2.00 + distance();
        return calculatedFare < 5.00 ? 5.00 : calculatedFare;
    }

    std::string rideType() const override {
        return "Shared";
    }
};

using RidePointer = std::shared_ptr<const Ride>;

class Driver {
public:
    Driver(std::string driverID, std::string name, double rating)
        : driverID_(std::move(driverID)), name_(std::move(name)), rating_(rating) {
        if (driverID_.empty() || name_.empty()) {
            throw std::invalid_argument("Driver ID and name cannot be empty.");
        }
        if (rating_ < 0.0 || rating_ > 5.0) {
            throw std::invalid_argument("Driver rating must be between 0 and 5.");
        }
    }

    void addRide(const RidePointer& ride) {
        if (!ride) {
            throw std::invalid_argument("Cannot assign an empty ride.");
        }
        assignedRides_.push_back(ride);
    }

    void getDriverInfo(std::ostream& output = std::cout) const {
        output << "Driver " << driverID_ << ": " << name_
               << " | Rating: " << std::fixed << std::setprecision(1)
               << rating_ << "/5.0\n"
               << "Completed rides: " << assignedRides_.size() << '\n';

        for (const auto& ride : assignedRides_) {
            ride->rideDetails(output);
        }
    }

private:
    std::string driverID_;
    std::string name_;
    double rating_;
    std::vector<RidePointer> assignedRides_;
};

class Rider {
public:
    Rider(std::string riderID, std::string name)
        : riderID_(std::move(riderID)), name_(std::move(name)) {
        if (riderID_.empty() || name_.empty()) {
            throw std::invalid_argument("Rider ID and name cannot be empty.");
        }
    }

    void requestRide(const RidePointer& ride) {
        if (!ride) {
            throw std::invalid_argument("Cannot request an empty ride.");
        }
        requestedRides_.push_back(ride);
    }

    void viewRides(std::ostream& output = std::cout) const {
        output << "Rider " << riderID_ << ": " << name_ << '\n'
               << "Requested rides: " << requestedRides_.size() << '\n';

        for (const auto& ride : requestedRides_) {
            ride->rideDetails(output);
        }
    }

private:
    std::string riderID_;
    std::string name_;
    std::vector<RidePointer> requestedRides_;
};

int main() {
    const auto morningCommute = std::make_shared<StandardRide>(
        "R-1001", "Central Library", "Tech Park", 4.8);
    const auto airportTransfer = std::make_shared<PremiumRide>(
        "R-1002", "Riverside Hotel", "City Airport", 11.25);
    const auto campusTrip = std::make_shared<SharedRide>(
        "R-1003", "North Campus", "Union Station", 6.4);
    const auto dinnerTrip = std::make_shared<PremiumRide>(
        "R-1004", "Art Museum", "Harbor Restaurant", 2.5);

    const std::vector<RidePointer> rides = {
        morningCommute, airportTransfer, campusTrip, dinnerTrip
    };

    std::cout << "RIDE SHARING SYSTEM - C++\n"
              << "========================================\n"
              << "All rides (polymorphic fare calculation)\n";

    double totalFare = 0.0;
    for (const auto& ride : rides) {
        totalFare += ride->fare();
        ride->rideDetails();
    }
    std::cout << "Combined fare: $" << std::fixed << std::setprecision(2)
              << totalFare << "\n\n";

    Driver driver("D-204", "Maya Chen", 4.9);
    driver.addRide(morningCommute);
    driver.addRide(airportTransfer);

    std::cout << "Driver record\n"
              << "----------------------------------------\n";
    driver.getDriverInfo();

    Rider rider("U-731", "Jordan Lee");
    rider.requestRide(morningCommute);
    rider.requestRide(campusTrip);
    rider.requestRide(dinnerTrip);

    std::cout << "\nRider history\n"
              << "----------------------------------------\n";
    rider.viewRides();

    return 0;
}
