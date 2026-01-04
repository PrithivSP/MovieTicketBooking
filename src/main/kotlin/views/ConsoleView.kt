package views

class ConsoleView {
    companion object {

        fun printHeader(header: String) {
            println("\n=====================================")
            println("         $header")
            println("=====================================\n")
        }

        fun printInvalidOptions() {
            println("Invalid choice.")
        }

        fun printEmptyInput() {
            println("Empty inputs. Try again.")
        }

        fun printInputNotValid(prompt: String) {
            println("Invalid $prompt format")
        }

        fun printGoBackMessage() {
            println("Enter 0 at any point to go back.")
        }

        fun printMessage(message: String) {
            println(message)
        }

        fun printError(errorMessage: String) {
            println("Error: $errorMessage")
        }
    }
}