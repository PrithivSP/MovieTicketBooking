package views

import models.Screen
import models.Seat
import models.SeatSnapShot
import models.Show
import models.enumerations.SeatTypes
import utils.readInt
import utils.readString
import java.time.LocalDate
import java.time.LocalTime
import java.util.TreeMap

class ShowView() {
    fun showNoShowsAvailable() {
        println("No shows available...")
    }

    fun showNoDatesAvailable() {
        println("No dates available...")
    }

    fun showNoTimesAvailable() {
        println("No Timings available...")
    }
    fun showDatesAndGetChoice(dates: List<LocalDate>): Int {

        ConsoleView.printHeader("Choose Date")

        dates.forEachIndexed { index, date ->
            println("${index + 1}) $date")
        }

        return readInt("Enter your choice (or 0 to go back): ")
    }

    fun showTimesAndGetChoice(times: List<LocalTime>): Int {
        ConsoleView.printHeader("Choose Time")

        times.forEachIndexed { index, date ->
            println("${index + 1}) $date")
        }

        return readInt("Enter your choice (or 0 to go back): ")
    }

    fun displaySeats(screen: Screen, show: Show) {
        println("Seat map(X = booked, O = available)")
        println("Legend: (P)=Premium (C)=Classic (E)=Economy\n")

        val rows = TreeMap<String, MutableList<String>>()

        val labels = screen.seatTypeMap.keys.sorted()

        for(label in labels) {
            val row = label.replace(Regex("\\d+$"), "")
            rows.computeIfAbsent(row) {
                mutableListOf()
            }.add(label)
        }

        for((row, seats) in rows) {
            print("$row ")

            for(label in seats) {

                val seatType = screen.seatTypeMap[label]
                val isBooked = show.seats.contains(label)

                val typeChar = when(seatType) {
                    SeatTypes.PREMIUM -> "P"
                    SeatTypes.CLASSIC -> "C"
                    SeatTypes.ECONOMY -> "E"
                    null -> "ERROR"
                }

                val mark = if(isBooked) "X" else "O"

                print("$label($typeChar)$mark ")
            }
            println()
        }
    }

    fun getSeatsInput(): MutableSet<String> {
        val seatInput = readString("\nEnter seat label to book (comma separated, e.g A1, A2) or 0 to cancel: ").trim().uppercase()

        if(seatInput == "0") return mutableSetOf()

        return seatInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableSet()

    }

    fun confirmSeatSelection(
        seatsSnapShot: List<SeatSnapShot>,
        totalPrice: Double
    ): String {
        println("\nBooking Summary")

        for(seat in seatsSnapShot) {
            println("${seat.seatLabel} | ${seat.seatType} | ${"%.2f".format(seat.seatPrice)}")
        }

        println("\nTotal: ${"%.2f".format(totalPrice)}")

        return readString("Confirm booking (y/n): ")
    }

    fun showInvalidSeats(invalidSeats: Set<String>) {
        print("Invalid Seats: ${invalidSeats.joinToString(", ")}")
    }

    fun showAlreadyBookedSeats(seats: Set<String>) {
        println("Already booked Seats: ${seats.joinToString(", ")}")
    }

}