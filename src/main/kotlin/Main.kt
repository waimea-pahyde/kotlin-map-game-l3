/**
 * =====================================================================
 * Programming Project for NCEA Level 3, Standard 91906
 * ---------------------------------------------------------------------
 * Project Name:   Eggplant Quest
 * Project Author: Polly Hyde
 * GitHub Repo:    https://github.com/waimea-pahyde/kotlin-map-game-l3
 * =====================================================================
 */


import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Font
import java.awt.Point
import javax.swing.*
import kotlin.math.sin


/**
 * Fun to scale images taken off Mr Copley
 */
fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =
    ImageIcon(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH))


fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val app = App()                 // Get an app state object
    val window = MainWindow(app)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


val map = mutableListOf<Location>()

/**
 * Manage app state
 */
class App {

    var currentLocation: Location

    var currentPlayer: Player = Player()

    init {

//        === LOCATION SETUP ===
        val cabin = Location("Cabin", "creepy old cabin")
        val duckPond = Location("Duck Pond", "large pond with ducks")
        val eggplantFields = Location("Eggplant Fields", "field full of eggplants.")
        val biggerEggplantFields = Location("The larger eggplant fields", "field full of smaller eggplants.")
        val creepyForest = Location("Jerry's Forest", "home to ???")


//        === WEAPON SETUP ===
        val jerrysLens = Weapon("Jerry's Lens", 2.0)
        val duckCatsBeak = Weapon("A Ducats Beak", 3.0)
        val womanMansSlice = Weapon("Woman Man's slice", 10.0)
        val swordOfFriendship = Weapon("Sword of Friendship", 15.0)
        val theFinalEggplant = Weapon("A ticket Home", 0.0)

//        === ENEMY SETUP ===
        val eyeOfJerry = Enemy(
            "Jerry",
            500,
            500,
            ImageIcon(ClassLoader.getSystemResource("images/cabin.jpg")),
            ImageIcon(ClassLoader.getSystemResource("images/jerry.png")),
            true,
            50,
            10,
            jerrysLens,
            ImageIcon(ClassLoader.getSystemResource("images/jerryslens.png")).scaled(200, 200),
        )

        val duckCat = Enemy(
            "David the Duck Cat",
            1000,
            1000,
            ImageIcon(ClassLoader.getSystemResource("images/ducklake.png")),
            ImageIcon(ClassLoader.getSystemResource("images/duckcat.png")),
            true,
            100,
            10,
            duckCatsBeak,
            ImageIcon(ClassLoader.getSystemResource("images/duckcatsbeak.png")).scaled(200, 200),
        )

        val womanMan = Enemy(
            "Woman-Man the Eggplant Fan",
            1500,
            1500,
            ImageIcon(ClassLoader.getSystemResource("images/eggplantfields.png")).scaled(1194, 834),
            ImageIcon(ClassLoader.getSystemResource("images/womanman.png")).scaled(200, 400),
            true,
            200,
            20,
            womanMansSlice,
            ImageIcon(ClassLoader.getSystemResource("images/slice.png")).scaled(200, 200)
        )
        val womanMen = Enemy(
            "Women-Men the Eggplant Friends",
            5000,
            5000,
            ImageIcon(ClassLoader.getSystemResource("images/eggplantfields.png")).scaled(1194, 834),
            ImageIcon(ClassLoader.getSystemResource("images/womenmen.png")).scaled(500, 350),
            true,
            300,
            30,
            swordOfFriendship,
            ImageIcon(ClassLoader.getSystemResource("images/swordoffriendship.png")).scaled(100, 200),
        )

        val jerry = Enemy(
            "Jerry",
            10000,
            10000,
            ImageIcon(ClassLoader.getSystemResource("images/thecreepyforest.png")).scaled(1194, 834),
            ImageIcon(ClassLoader.getSystemResource("images/aubajerry.png")).scaled(800, 600),
            true,
            500,
            50,
            theFinalEggplant,
            ImageIcon(ClassLoader.getSystemResource("images/thefinaleggplant.png")).scaled(200, 200),
        )


//        === Adding locations to the map ===
        map.add(cabin)
        map.add(duckPond)
        map.add(eggplantFields)
        map.add(biggerEggplantFields)
        map.add(creepyForest)

//      === Adding enemies to the locations ===
        cabin.addEnemy(eyeOfJerry)
        duckPond.addEnemy(duckCat)
        eggplantFields.addEnemy(womanMan)
        biggerEggplantFields.addEnemy(womanMen)
        creepyForest.addEnemy(jerry)

//      === Adding dialogue to the enemies ===
        eyeOfJerry.addDialogue("I am not who you seek")
        eyeOfJerry.addDialogue("But I shall not let you leave until we have finsihed")
        eyeOfJerry.addDialogue("The Master Plan")

        duckCat.addDialogue("quack (I am the guard of this place)")
        duckCat.addDialogue("quack quack (And you're supposed to be inside...)")
        duckCat.addDialogue("QUACH QUACKKKK (FACE MY WRATH!)")

        womanMan.addDialogue("I am woman man the eggplant fan")
        womanMan.addDialogue("A person of indeterminate gender but")
        womanMan.addDialogue("by god do I love eggplants.")
        womanMan.addDialogue("and you've tresspassed on my fields.")

        womanMen.addDialogue("You.. You killed woman man.")
        womanMen.addDialogue("We'll defeat you with the power of friendship!")

        jerry.addDialogue("Well hello there.")
        jerry.addDialogue("Did you have a nice sleep?")
        jerry.addDialogue("Youre probably wondering why I brought you here.")
        jerry.addDialogue("Look around you. Your love for eggplants has DESTROYED our lands")
        jerry.addDialogue("So now I'm going to mince you;")
        jerry.addDialogue("and turn you into an eggplant stew")
        jerry.addDialogue("Doesn't that sound nice?")

//        === Set the current location to the cabin so the game starts in the right spot===
        currentLocation = map[0]
    }

    /**
     * function to take enemy damage.
     */
    fun takeDamage() {

        //Calculates the damage taken
        currentLocation.listOfEnemies[0].calculateDamage(currentPlayer)

//        If the enemy is alive, then take the damage
        if (currentLocation.listOfEnemies[0].alive) {
            currentLocation.listOfEnemies[0].enemyCurrentHP -= currentLocation.listOfEnemies[0].damageTaken

//          Checks if the enemy's health has gone below zero and if so sets it equal.
            if (currentLocation.listOfEnemies[0].enemyCurrentHP < 0) currentLocation.listOfEnemies[0].enemyCurrentHP = 0

//          Checks if alive
            currentLocation.listOfEnemies[0].doIBreathe()
        }

        if (!currentLocation.listOfEnemies[0].alive) {

//           If not alive, completes the location which signals the main window to spawn the weapon dropped.
            currentLocation.complete = true

        }
    }

    /**
     * Function to take damage away from the player.
     * Has a chance not to hit the player, but as the player gets further and further through,
     * the chance to hit increases. Includes a random element to
     */
    fun doPlayerDamage() {
//        Calculates a random number between 10-50 and adds it onto the base hit chance of that enemy
        val playerHitMultiplier = (10..50).random()
        val playerHit = (currentLocation.listOfEnemies[0].hitChance + playerHitMultiplier)

//        Could've been a when, but due to time constraints wasn't.
        if (playerHit > 50) {
            currentPlayer.currentHealth -= currentLocation.listOfEnemies[0].damage
        }
//        If it's less than zero, kill the player, which sends the signal to the UI.
        if (currentPlayer.currentHealth <= 0) {
            currentPlayer.alive = false
            return
        }
//        If the enemy is dead, and the location is 'complete', set the players health to max again.
        if (currentLocation.complete) {
            currentPlayer.currentHealth = currentPlayer.health
        }
    }

    /**
     * Function to go to the next room
     * Invoked by a button in the Main window. If out of bounds, returns false so as the main window can display an error.
     * Returns: Boolean - true if room is in range
     */
    fun goRight(): Boolean {
        if (map.indexOf(currentLocation) + 1 !in map.indices) return false
        var newLocation = map.indexOf(currentLocation)
        newLocation += 1

        currentLocation = map[newLocation]

        return true

    }

    /**
     * Function to go back to the previous room
     * Invoked by a button in the Main window. If out of bounds, returns false so as the main window can display an error.
     * Returns: Boolean - true if room is in range
     */
    fun goLeft(): Boolean {
        if (map.indexOf(currentLocation) == 0) return false

        var newLocation = map.indexOf(currentLocation)
        newLocation -= 1
        currentLocation = map[newLocation]
        return true
    }
}


/**
 * Main UI window, handles user clicks, etc.
 *
 * @param app the app state object
 */
class MainWindow(private val app: App) {

    private val frame = JFrame("Eggplant Quest")
    private val panel = JLayeredPane().apply { layout = null }

    // ==== Full screens ====
    private val titleLabel = JLabel(app.currentLocation.name)
    private val actualTitleScreen =
        JLabel(ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/titlescreen.png")).scaled(1194, 834))
    private val deathScreen =
        JLabel(ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/deathscreen.png")).scaled(1194, 834))
    private val winScreen =
        JLabel(ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/winscreen.png")).scaled(1194, 834))

    private var titleScreens = mutableListOf<JLabel>()

    //    ======Enemy Images=======
    private val jerryBackground = ImageIcon(ClassLoader.getSystemResource("images/cabin.jpg"))
    private val background = JLabel(jerryBackground)

    private val enemyName = JLabel(app.currentLocation.listOfEnemies[0].enemyName)

    private val enemyHealthBar =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/enemyhealth.png")).scaled(600, 400))
    private val enemyHealth =
        JLabel("${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}")

    private val enemyItself = JButton(app.currentLocation.listOfEnemies[0].enemyForm.scaled(500, 500))

    private val droppedWeapon = JButton(app.currentLocation.listOfEnemies[0].weaponImage)

    //    ==== UI Elements ====
    private val leftArrow = ImageIcon(ClassLoader.getSystemResource("images/arrowpointingright.png"))
    private val goRightButton = JButton(ImageIcon(ClassLoader.getSystemResource("images/arrow.png")).scaled(90, 90))
    private val goLeftButton = JButton(leftArrow.scaled(90, 90))

    private val playerHealth = JLabel("${app.currentPlayer.currentHealth}/${app.currentPlayer.health}")
    private val playerHealthBar =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/playerhealth.png")).scaled(300, 500))

    private val titleBarImage = ImageIcon(ClassLoader.getSystemResource("images/titlebar.png"))
    private val titleBackground = JLabel(titleBarImage)

    private val outOfRangeError = JLabel("You can't go that way!")

    private val startButton =
        JButton(ImageIcon(ClassLoader.getSystemResource("images/startbutton.png")).scaled(600, 500))

    private val playerScoreLabel = JLabel("Player Score")

    //    ==== Timers ====
    private val dialogueTimer = Timer(4000, null)
    private val titleScreenTimer = Timer(4000, null)
    private val playerHealTimer = Timer(100, null)
    private val scoreTimer = Timer(1000, null)

    //    ==== DIALOGUE BOXES ====
    private val placeDialogue = JLabel("This is a place")
    private val placeDialogueBackground =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/transparentdialogue.png")).scaled(600, 300))

    private val damageDialogueMessage =
        JLabel("You did ${app.currentLocation.listOfEnemies[0].damageTaken} damage to ${app.currentLocation.listOfEnemies[0].enemyName}")
    private val damageDialogueBox =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/damagedialogue.png")).scaled(600, 250))

    private var enemyDialogueBackground =
        JLabel(ImageIcon(ClassLoader.getSystemResource("images/transparentdialogue.png")).scaled(600, 300))
    private var enemyDialogue =
        JButton("${app.currentLocation.listOfEnemies[0].enemyName}:\"${app.currentLocation.listOfEnemies[0].listOfDialogues[0]}\"")

    private var weaponDialogueMessage = JLabel()

//    ======================

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
        dialogueTimer.isRepeats = false

    }


    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1194, 834)

        background.setBounds(0, 0, 1194, 834)
        actualTitleScreen.setBounds(0, 0, 1194, 834)
        winScreen.setBounds(0, 0, 1194, 834)
        deathScreen.setBounds(0, 0, 1194, 834)

        titleLabel.setBounds(350, 40, 500, 30)
        titleBackground.setBounds(230, 10, 700, 100)
        enemyName.isVisible = false

        startButton.setBounds(230, 400, 800, 200)

        goRightButton.setBounds(250, 10, 100, 100)
        goLeftButton.setBounds(800, 15, 100, 100)

        enemyName.setBounds(30, 50, 30, 30)

        enemyHealth.setBounds(50, 625, 300, 30)
        enemyHealthBar.setBounds(-100, 600, 500, 100)

        playerHealth.setBounds(50, 520, 300, 30)
        playerHealthBar.setBounds(-150, 500, 500, 100)

        enemyItself.setBounds(300, 200, 500, 500)

        placeDialogue.setBounds(430, 350, 300, 50)
        placeDialogueBackground.setBounds(330, 230, 600, 300)

        enemyDialogue.setBounds(250, 550, 600, 300)
        enemyDialogueBackground.setBounds(300, 550, 600, 300)

        outOfRangeError.setBounds(200, 250, 300, 100)

        droppedWeapon.setBounds(500, 600, 200, 200)

        damageDialogueMessage.setBounds(480, 150, 1000, 100)
        damageDialogueBox.setBounds(370, 100, 400, 200)

        playerScoreLabel.setBounds(480, 150, 400, 400)

        weaponDialogueMessage.setBounds(480, 150, 1000, 100)


        panel.add(actualTitleScreen, JLayeredPane.DEFAULT_LAYER)
        panel.add(startButton, JLayeredPane.DEFAULT_LAYER + 1)
        panel.setLayer(startButton, JLayeredPane.DEFAULT_LAYER + 1)


    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        titleLabel.horizontalAlignment = JLabel.CENTER


        enemyItself.isBorderPainted = false
        enemyItself.isFocusPainted = false
        enemyItself.isContentAreaFilled = false

        droppedWeapon.isBorderPainted = false
        droppedWeapon.isFocusPainted = false
        droppedWeapon.isContentAreaFilled = false

        goLeftButton.isFocusPainted = false
        goRightButton.isFocusPainted = false
        goLeftButton.isContentAreaFilled = false
        goRightButton.isContentAreaFilled = false
        goLeftButton.isBorderPainted = false
        goRightButton.isBorderPainted = false

        startButton.isFocusPainted = false
        startButton.isContentAreaFilled = false
        startButton.isBorderPainted = false

        enemyDialogue.isBorderPainted = false
        enemyDialogue.isContentAreaFilled = false
        enemyDialogue.isFocusPainted = false

    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun setupActions() {

        enemyItself.addActionListener { handleEnemyClick() }

        goRightButton.addActionListener { handleGoRightClick() }
        goLeftButton.addActionListener { handleGoLeftClick() }

        droppedWeapon.addActionListener { handleWeaponClick() }

//        Two action listeners as this could be called for an error or for a dialogue.
        dialogueTimer.addActionListener { closeError() }
        dialogueTimer.addActionListener { closeDialogue() }

        enemyDialogue.addActionListener { handleDialogueClick() }

        startButton.addActionListener { titleScreen() }

        scoreTimer.addActionListener { score++ }

        playerHealTimer.addActionListener { healPlayer() }
    }

    private var score = 0
    private var currentTitleScreen = 0
    private var lastTitleScreen = false
    private var indexOfCurrentDialogue = 0
    private var lastDialogue = false


    /**
     * Invoked by the dialogue at the start of the fight
     * Handles the enemies' dialogue, and if on the last one, starts the room.
     */
    private fun handleDialogueClick() {
        if (lastDialogue) { // Checking for game start
            panel.remove(enemyDialogue)
            panel.remove(background) //The background that gets overlaid to cover the ui elements.
            enemyItself.isVisible = true
            enemyItself.isEnabled = true
            playerHealth.isVisible = true
            panel.remove(enemyDialogueBackground)
            panel.add(background, JLayeredPane.DEFAULT_LAYER - 2) //Adds it back where it should be.
            panel.repaint()
            panel.revalidate()
            return
        }


        enemyDialogue.text =
            ("${app.currentLocation.listOfEnemies[0].enemyName}:\"${app.currentLocation.listOfEnemies[0].listOfDialogues[indexOfCurrentDialogue]}\"")
        indexOfCurrentDialogue++


        panel.revalidate()
        panel.repaint()

//        If last dialogue, sets the variable to true, so it does the last dialogue instead of the second last and starting the room.
        if (indexOfCurrentDialogue >= app.currentLocation.listOfEnemies[0].listOfDialogues.size) {
            lastDialogue = true
        }


    }

    /**
     * Gets called by the timer started in gameStart function. After that, adds 10 health a second.
     */
    private fun healPlayer() {
        if (app.currentPlayer.currentHealth <= 999) {

            app.currentPlayer.currentHealth++
            updateUI()
        }
    }

    /**
     * Progresses through the title screens to give the game context. Checks for the last title screen.
     * If it is the last title screen, starts the game.
     */
    private fun handleTitleScreenTimer() {
        if (lastTitleScreen) {
            titleScreenTimer.stop()
            panel.remove(titleScreens[currentTitleScreen - 1])
            startGame()
            return
        }

        panel.add(titleScreens[currentTitleScreen])
        if (currentTitleScreen != 0) panel.remove(titleScreens[currentTitleScreen - 1]) // If it isn't the first title screen, removes the previous title screen.
        panel.revalidate()
        panel.repaint()
        currentTitleScreen++

        if (currentTitleScreen >= titleScreens.size) {
            lastTitleScreen = true
        }

    }

    /**
     * When starting a new room, run through the list of dialogue that each enemy has.
     * Adds a background on top to hide some ui elements that are disabled for clarity that they aren't dead
     * Also shows the place dialogue, announcing to the player they have enetered a new room.
     */
    private fun doRoomDialogue() {
        panel.add(background, JLayeredPane.DEFAULT_LAYER + 4)
        panel.setLayer(titleBackground, JLayeredPane.DEFAULT_LAYER + 5)
        panel.setLayer(
            titleLabel,
            JLayeredPane.DEFAULT_LAYER + 6
        ) // Adds the background on top of everything, and the title on top of that
        // Hides the arrows
        goLeftButton.isVisible = false
        goRightButton.isVisible = false

        placeDialogue.text =
            "<html>You travel left, and arrive at the ${app.currentLocation.name}, a ${app.currentLocation.description}.<html>"
        showPlaceDialogue()
        updateUI()

        // Adds the first enemy dialogue, because it's a button that cycles through the rest of them.
        panel.add(enemyDialogue, JLayeredPane.DEFAULT_LAYER + 4)
        panel.setLayer(enemyDialogue, JLayeredPane.DEFAULT_LAYER + 4)
        panel.add(enemyDialogueBackground, JLayeredPane.DEFAULT_LAYER)
        indexOfCurrentDialogue = 0
        lastDialogue = false
        enemyDialogue.text =
            ("${app.currentLocation.listOfEnemies[0].enemyName}:\"${app.currentLocation.listOfEnemies[0].listOfDialogues[indexOfCurrentDialogue]}\"")
        handleDialogueClick()
    }

    /**
     * Adds all the title screens to a mutable list of title screens. Function gets called when
     * the start button is pressed.
     *
     * Starts the title screen timer, who then cycles through the rest of them & starts
     * the game.
     */
    fun titleScreen() {
        panel.removeAll() // Removing start screen


        val titleScreen1 = JLabel(ImageIcon(ClassLoader.getSystemResource("images/titleScreen1.png")).scaled(1194, 834))
        val titleScreen2 = JLabel(ImageIcon(ClassLoader.getSystemResource("images/titleScreen2.png")).scaled(1194, 834))
        val titleScreen3 = JLabel(ImageIcon(ClassLoader.getSystemResource("images/titleScreen3.png")).scaled(1194, 834))
        val titleScreen4 = JLabel(ImageIcon(ClassLoader.getSystemResource("images/titleScreen4.png")).scaled(1194, 834))
        val titleScreen5 = JLabel(ImageIcon(ClassLoader.getSystemResource("images/titleScreen5.png")).scaled(1194, 834))
        titleScreens.add(titleScreen1)
        titleScreens.add(titleScreen2)
        titleScreens.add(titleScreen3)
        titleScreens.add(titleScreen4)
        titleScreens.add(titleScreen5)

        titleScreen1.setBounds(0, 0, 1194, 834)
        titleScreen2.setBounds(0, 0, 1194, 834)
        titleScreen3.setBounds(0, 0, 1194, 834)
        titleScreen4.setBounds(0, 0, 1194, 834)
        titleScreen5.setBounds(0, 0, 1194, 834)

        titleScreenTimer.addActionListener { handleTitleScreenTimer() }



        titleScreenTimer.start()
        handleTitleScreenTimer()

    }


    /**
     * Gets called after the last panel of the title screen.
     * Adds the enemy and other ui elements to the panel
     * Starts the score timer
     * Starts the Cabin's room dialogue.
     */
    private fun startGame() {
        panel.add(enemyName, JLayeredPane.DEFAULT_LAYER)
        panel.add(enemyHealth, JLayeredPane.DEFAULT_LAYER)
        panel.add(enemyHealthBar, JLayeredPane.DEFAULT_LAYER - 1)
        playerHealth.isVisible = false
        panel.add(enemyItself, JLayeredPane.DEFAULT_LAYER)
        enemyItself.isVisible = false
        enemyItself.isEnabled = false
        panel.add(goRightButton, JLayeredPane.DEFAULT_LAYER)
        panel.add(goLeftButton, JLayeredPane.DEFAULT_LAYER)

        panel.add(playerHealth, JLayeredPane.DEFAULT_LAYER)
        panel.add(playerHealthBar, JLayeredPane.DEFAULT_LAYER - 1)

        panel.add(titleLabel, JLayeredPane.DEFAULT_LAYER + 1)
        panel.add(titleBackground, JLayeredPane.DEFAULT_LAYER - 1)

        panel.add(background, JLayeredPane.DEFAULT_LAYER - 1)

        panel.revalidate()
        panel.repaint()
        playerHealTimer.start()
        scoreTimer.start()
        doRoomDialogue()

        JOptionPane.showMessageDialog(
            frame,
            "Click the dialogue to continue!",
            "Tip!",
            JOptionPane.INFORMATION_MESSAGE
        )

    }

    /**
     * Gets called after the dialogue timer ends. Removes the damage, place & weapon
     * dialogue all at once, even if they were added at different times.
     *
     * Repaints & revalidates.
     *
     */
    private fun closeDialogue() {
        panel.remove(damageDialogueMessage)
        panel.remove(damageDialogueBox)
        panel.remove(placeDialogueBackground)
        panel.remove(placeDialogue)
        panel.remove(weaponDialogueMessage)

        panel.revalidate()
        panel.repaint()
    }

    /**
     * Uses the same timer as the previous funtion but only closes the out of range error
     * that pops up when attempting to go out of the map on the right side.
     */
    private fun closeError() {
        panel.remove(outOfRangeError)
        panel.revalidate()
        panel.repaint()
    }

    /**
     * Shows the damage dialogue, then starts the timer to close it.
     * Gets called after damaging an enemy.
     */
    private fun showDialogue() {
        panel.add(damageDialogueBox, JLayeredPane.DEFAULT_LAYER)
        panel.add(damageDialogueMessage, JLayeredPane.DEFAULT_LAYER)


        dialogueTimer.start()
        panel.revalidate()
        panel.repaint()
    }

    /**
     * function to show the place dialogue when entering a room.
     * removes the previous damage dialogue, adds the place dialogue in its place.
     * Starts the timer to remove itself.
     */
    private fun showPlaceDialogue() {
        panel.remove(damageDialogueMessage)
        panel.remove(damageDialogueBox)
        panel.add(placeDialogue, JLayeredPane.DEFAULT_LAYER + 1)
        panel.add(placeDialogueBackground, JLayeredPane.DEFAULT_LAYER + 2)
        dialogueTimer.start()
        panel.revalidate()
        panel.repaint()


    }

    /**
     * Function to handle the weapon once it's been clicked on by the player.
     * Sets it as the weapon, and shows some dialogue about it
     */
    private fun handleWeaponClick() {
        app.currentPlayer.currentWeapon =
            app.currentLocation.listOfEnemies[0].weaponDropped // Sets the players weapon to the weapon that the now-dead enemy drops

        weaponDialogueMessage.text = "You picked up ${app.currentLocation.listOfEnemies[0].weaponDropped.name}"

        panel.add(
            damageDialogueBox,
            JLayeredPane.DEFAULT_LAYER + 3
        ) // Reuses the damage dialogue box as the weapon dialogue box
        panel.add(weaponDialogueMessage, JLayeredPane.DEFAULT_LAYER + 5)
        panel.setLayer(weaponDialogueMessage, JLayeredPane.DEFAULT_LAYER + 5)

        // Enable both the left and right buttons that were disabled when the weapon was dropped
        goLeftButton.isEnabled = true
        goRightButton.isEnabled = true

        dialogueTimer.start()   // Starts the dialogue timer for above

        panel.remove(droppedWeapon)
        panel.revalidate()
        panel.repaint()
    }

    /**
     * Function to do damage to both the player and enemy once the enemy's been clicked on
     */
    private fun handleEnemyClick() {
        updateUI()  // Double checks everything's updated
        app.takeDamage()       // Does damage to the enemy
        updateUI()  // Updates his healthbar
        makeButtonShake(enemyItself)
        showDialogue() // Shows the damage dialogue with how much damage the enemy took.
        winGameCheck()

        app.doPlayerDamage()
        updateUI()
    }

    /**
     * Function to go to the next room in the map.
     */
    private fun handleGoRightClick() {
        val inRange = app.goRight()
        when (inRange) {
            true -> {
                updateUI()
                if (app.currentLocation.listOfEnemies[0].alive) {   // Checking if the enemy has been killed previously, and you're going back through.
                    doRoomDialogue()

                }
            }

            false -> {
                panel.add(outOfRangeError)
                panel.setComponentZOrder(outOfRangeError, 1)
                panel.revalidate()
                panel.repaint()
                updateUI()

            }
        }
        updateUI()
    }

    /**
     * Function to go back to the previous room.
     */
    private fun handleGoLeftClick() {
        val inRange = app.goLeft()

        when (inRange) {
            true -> {
                updateUI()

            }

            false -> {
                panel.add(outOfRangeError)
                dialogueTimer.start()
                panel.setComponentZOrder(outOfRangeError, 1)
                panel.revalidate()
                panel.repaint()
                updateUI()
            }
        }
        updateUI()
    }

    /**
     * Function to make a button shake. Partial credit to stack overflow for the 'sin' movement.
     *
     * Arguments: button (JButton) - The button that will be shook.
     */
    private fun makeButtonShake(button: JButton) {
        val originalLocation: Point = button.location
        val shakeDistance = 10
        val shakeDuration = 400
        val shakeFrequency = 20
        val startTime = System.currentTimeMillis()

        Timer(shakeFrequency) { timerEvent ->
            val elapsed =
                System.currentTimeMillis() - startTime // Takes the current time off the original time rather tha nstarting a timer.
            if (elapsed > shakeDuration) {
                button.location = originalLocation
                (timerEvent.source as Timer).stop()
            } else {

                val offset = (sin(elapsed.toDouble() * 0.1) * shakeDistance).toInt()
                button.setLocation(originalLocation.x + offset, originalLocation.y)
            }
        }.start()
    }

    /**
     * Function to end the game.
     * Takes everything off the screen and adds the death screen.
     */
    private fun endGame() {
        panel.removeAll()
        panel.add(deathScreen)

        panel.revalidate()
        panel.repaint()
    }

    /**
     * Function to check if the game is run by running through all of the locations
     * If they're all clear run the winGame()
     */
    private fun winGameCheck() {
        for (location in map) {
            if (!location.complete) return
        }
        winGame()
    }

    /**
     * Function to show the win game screen & final score
     */
    private fun winGame() {
        panel.removeAll()
        panel.add(winScreen)
        playerScoreLabel.text = "Final time: $score"
        panel.add(playerScoreLabel, JLayeredPane.DEFAULT_LAYER + 5)
        panel.setLayer(playerScoreLabel, JLayeredPane.DEFAULT_LAYER + 5)
        panel.revalidate()
        panel.repaint()
    }

    /**
     *Function to handle most dynamic & non-specific ui changes
     *
     */
    private fun updateUI() {

        damageDialogueMessage.text =
            "You did ${app.currentLocation.listOfEnemies[0].damageTaken} damage to ${app.currentLocation.listOfEnemies[0].enemyName}!"

        if (!app.currentPlayer.alive) endGame()

        playerHealth.text = "${app.currentPlayer.currentHealth}/${app.currentPlayer.health}"

        droppedWeapon.text = app.currentLocation.listOfEnemies[0].weaponDropped.name

        background.icon = (app.currentLocation.listOfEnemies[0].background)
        enemyItself.icon = (app.currentLocation.listOfEnemies[0].enemyForm)

        droppedWeapon.icon = (app.currentLocation.listOfEnemies[0].weaponImage)

        titleLabel.text = app.currentLocation.name


        when (app.currentLocation.complete) {

            false -> { // If the location isn't complete, make sure the player can't move locations without winning the fight
                goRightButton.isEnabled = false
                goLeftButton.isEnabled = false
            }

            true -> {
                playerHealth.isVisible = false
                enemyItself.isVisible = false
                enemyItself.isEnabled = false
                goLeftButton.isVisible = true
                goRightButton.isVisible = true
                damageDialogueMessage.text = "You killed ${app.currentLocation.listOfEnemies[0].enemyName}!"

                if (!app.currentLocation.listOfEnemies[0].spawnedWeapon) { // if the weapon hasn't been spawned yet, spawn it in.

                    app.currentLocation.listOfEnemies[0].spawnedWeapon = true
                    panel.add(droppedWeapon, JLayeredPane.DEFAULT_LAYER + 1)
                    panel.revalidate()
                    panel.repaint()
                }
            }
        }

        enemyName.text = (app.currentLocation.listOfEnemies[0].enemyName)
        enemyHealth.text =
            "${app.currentLocation.listOfEnemies[0].enemyCurrentHP}/${app.currentLocation.listOfEnemies[0].enemyMaxHP}"
    }

    fun show() {
        frame.isVisible = true
    }

}

/**
 * ENEMY CLASS
 *  Creates enemys before they're added to room
 *
 */
class Enemy(
    val enemyName: String,
    val enemyMaxHP: Int,
    var enemyCurrentHP: Int,

    val background: ImageIcon,
    val enemyForm: ImageIcon,


    var alive: Boolean = true,

    val damage: Int,

    val hitChance: Int,

    val weaponDropped: Weapon,

    val weaponImage: ImageIcon,


    var damageTaken: Int = 0,
) {
    var spawnedWeapon = false

    val listOfDialogues = mutableListOf<String>()

    //  Function to check if enemy is alive.
    fun doIBreathe() {
        if (enemyCurrentHP == 0) {
            alive = false
        }

    }

    /**
     * Adds dialogue to the enemys list of dialogues
     * Arguments: dialogue (string) - The text to be added
     */
    fun addDialogue(dialogue: String) {
        listOfDialogues.add(dialogue)
    }

    /**
     * Function to create a random amount of damage for the enemy to take.
     * Arguments: player (Player) - The current player playing.
     */
    fun calculateDamage(player: Player) {

        val randomDamageElement = (10..50).random()

        val totalDamage = (player.calculateDamage() + randomDamageElement)
        damageTaken = totalDamage

    }
}

/**
 * Locations that the enemys will exist in.
 */
class Location(
    val name: String,
    val description: String,
    var complete: Boolean = false
) {
    val listOfEnemies = mutableListOf<Enemy>()
    fun addEnemy(enemy: Enemy) {
        listOfEnemies.add(enemy)
    }

}

/**
 * Makes a player.
 */
class Player(
    private val baseDamage: Int = 100,
    val health: Int = 1000,
    var currentHealth: Int = 1000,
    var alive: Boolean = true,
    var currentWeapon: Weapon = Weapon("Sharp Stick", 1.0)
) {

    /**
     * Function to calculate the amount of damage the player will do
     * Returns: Int - The amount of damage the player will do.
     */
    fun calculateDamage(): Int {
        val finalDamage = baseDamage * currentWeapon.damageMultiplier.toInt()
        return finalDamage
    }
}

/**
 * Class that makes weapons.
 */
class Weapon(
    val name: String,
    val damageMultiplier: Double,
)