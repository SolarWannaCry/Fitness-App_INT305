package com.example.fitness

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import com.example.fitness.ui.theme.FitnessTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessTheme {
                Fitness()

            }
        }
    }
}

@Composable
fun Fitness() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController = navController)
        }
    }
}
@Composable
fun HomeScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fitness App", style = MaterialTheme.typography.h4)
        Icon(Icons.Default.FitnessCenter, contentDescription = "App Logo", modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate(Screen.Workout.route) }) {
            Text("Sign In")
        }

        if (showDialog) {
            SignUpDialog(navController = navController, onDismissRequest = { showDialog = false })
        }
    }
}

@Composable
fun SignUpDialog(navController: NavHostController, onDismissRequest: () -> Unit) {
    val (firstName, setFirstName) = remember { mutableStateOf("") }
    val (lastName, setLastName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val sexOptions = listOf("Male", "Female", "Other")
    val (selectedSex, setSelectedSex) = remember { mutableStateOf(sexOptions.first()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Sign Up") },
        text = {
            Column {
                TextField(value = firstName, onValueChange = setFirstName, label = { Text("First Name") })
                TextField(value = lastName, onValueChange = setLastName, label = { Text("Last Name") })
                DropdownMenuBox(sexOptions, selectedSex, setSelectedSex)
                TextField(value = email, onValueChange = setEmail, label = { Text("Email") })
                TextField(value = password, onValueChange = setPassword, label = { Text("Password") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Add sign-up logic here
                    // Navigate to the workout page upon successful sign-up
                    navController.navigate(Screen.Workout.route)
                }
            ) {
                Text("Submit")
            }
        }
    )
}



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Workout,
        Screen.Nutrition,
        Screen.Progress,
        Screen.Community
    )
    // Observe the current back stack entry
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    // Determine the current route
    val currentRoute = navBackStackEntry.value?.destination?.route


    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {

                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable(Screen.Workout.route) { WorkoutSection() }
        composable(Screen.Nutrition.route) { NutritionSection() }
        composable(Screen.Progress.route) { ProgressSection() }
        composable(Screen.Community.route) { CommunitySection() }
    }
}

// UI sections
@Composable
fun WorkoutSection() {
    val workoutGoals = listOf(
        WorkoutGoal(
            name = "Get Stronger",
            exercises = listOf(
                Exercise("Squat", "3 sets of 5 reps", "Barbell", "Foundation for lower body strength."),
                Exercise("Deadlift", "3 sets of 5 reps", "Barbell", "Improves overall body strength."),
                Exercise("Bench Press", "3 sets of 5 reps", "Barbell", "Key exercise for chest strength."),
                Exercise("Overhead Press", "3 sets of 5 reps", "Dumbbell", "Develops shoulder muscles."),
                Exercise("Barbell Row", "3 sets of 5 reps", "Barbell", "Strengthens the back."),
                Exercise("Pull-Up", "3 sets of 8 reps", "Bodyweight", "Enhances upper body strength."),
                Exercise("Plank", "3 sets of 60 seconds", "Bodyweight", "Core strengthening exercise.")
            )
        ),
        WorkoutGoal(
            name = "Increase Muscle Mass",
            exercises = listOf(
                Exercise("Dumbbell Curl", "4 sets of 10-12 reps", "Dumbbell", "Builds biceps."),
                Exercise("Tricep Extension", "4 sets of 10-12 reps", "Dumbbell", "Targets the triceps."),
                Exercise("Leg Press", "4 sets of 10-12 reps", "Machine", "Increases leg mass."),
                Exercise("Chest Fly", "4 sets of 10-12 reps", "Dumbbell", "Expands the chest."),
                Exercise("Lateral Raise", "4 sets of 10-12 reps", "Dumbbell", "Defines shoulder muscles."),
                Exercise("Squat", "4 sets of 10-12 reps", "Barbell", "Builds lower body mass."),
                Exercise("Deadlift", "4 sets of 10-12 reps", "Barbell", "Increases back and leg muscle mass.")
            )
        ),
        WorkoutGoal(
            name = "Get Lean",
            exercises = listOf(
                Exercise("Jump Rope", "4 sets of 3 minutes", "Rope", "Boosts cardio and burns fat."),
                Exercise("Burpees", "4 sets of 15 reps", "Bodyweight", "Full-body exercise that burns calories."),
                Exercise("Mountain Climbers", "4 sets of 45 seconds", "Bodyweight", "Increases heart rate and targets the core."),
                Exercise("Kettle bell Swings", "4 sets of 20 reps", "Kettlebell", "Improves power and burns fat."),
                Exercise("Running", "30 minutes", "Treadmill or Outdoor", "Effective cardio for fat loss."),
                Exercise("Bicycle Crunches", "4 sets of 20 reps", "Bodyweight", "Targets abdominal muscles."),
                Exercise("High Knees", "4 sets of 45 seconds", "Bodyweight", "Increases heart rate and tones legs.")
            )
        ),
        WorkoutGoal(
            name = "General Fitness",
            exercises = listOf(
                Exercise("Push-Ups", "3 sets of 10-15 reps", "Bodyweight", "Strengthens the upper body."),
                Exercise("Squats", "3 sets of 10-15 reps", "Bodyweight", "Builds lower body strength."),
                Exercise("Planks", "3 sets of 60 seconds", "Bodyweight", "Core strengthening."),
                Exercise("Lunges", "3 sets of 10 reps per leg", "Bodyweight", "Enhances leg strength and balance."),
                Exercise("Dumbbell Rows", "3 sets of 10-12 reps", "Dumbbell", "Back strengthening."),
                Exercise("Sit-Ups", "3 sets of 15 reps", "Bodyweight", "Core workout."),
                Exercise("Jumping Jacks", "3 sets of 30 seconds", "Bodyweight", "Improves cardiovascular health.")
            )
        ),
        WorkoutGoal(
            name = "Power Lifting",
            exercises = listOf(
                Exercise("Squat", "5 sets of 5 reps", "Barbell", "Core lift for leg and back strength."),
                Exercise("Bench Press", "5 sets of 5 reps", "Barbell", "Essential upper body strength exercise."),
                Exercise("Dead lift", "5 sets of 5 reps", "Barbell", "Targets the back, glutes, and legs."),
                Exercise("Overhead Press", "4 sets of 6 reps", "Barbell", "Develops shoulder and arm strength."),
                Exercise("Barbell Row", "4 sets of 6 reps", "Barbell", "Strengthens the back."),
                Exercise("Pull-Ups", "3 sets to failure", "Bodyweight", "Enhances upper body strength."),
                Exercise("Dips", "3 sets to failure", "Bodyweight", "Targets triceps and chest.")
            )
        ),
        WorkoutGoal(
            name = "Cardio",
            exercises = listOf(
                Exercise("Running", "45 minutes", "Outdoor or Treadmill", "Improves cardiovascular endurance."),
                Exercise("Cycling", "45 minutes", "Stationary Bike", "Low impact cardio workout."),
                Exercise("Swimming", "30 minutes", "Pool", "Full-body workout and cardiovascular improvement."),
                Exercise("Rowing", "30 minutes", "Rowing Machine", "Targets upper and lower body."),
                Exercise("Stair Climber", "30 minutes", "Machine", "Builds endurance and leg strength."),
                Exercise("HIIT Circuit", "20 minutes", "Various", "High intensity interval training for fat loss."),
                Exercise("Jump Rope", "15 minutes", "Rope", "Improves coordination and stamina.")
            )
        )
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(workoutGoals) { goal ->
            GoalItem(goal)
        }
    }
}

@Composable
fun GoalItem(goal: WorkoutGoal) {
    var toggleState by remember { mutableStateOf(goal.isActive) }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Text(goal.name, style = MaterialTheme.typography.h6, modifier = Modifier.weight(1f))
            Switch(
                checked = toggleState,
                onCheckedChange = { newState ->
                    toggleState = newState
                    goal.isActive = newState
                }
            )
        }
        if (goal.isActive) {
            Box(modifier = Modifier.heightIn(max = 200.dp)) {
                LazyColumn {
                    items(goal.exercises) { exercise ->
                        ExerciseItem(exercise)
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise) {
    Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
        Text(exercise.name, style = MaterialTheme.typography.subtitle1)
        Text("Sets/Reps: ${exercise.setsReps}", style = MaterialTheme.typography.body1)
        Text("Equipment: ${exercise.equipment}", style = MaterialTheme.typography.body1)
        Text("Description: ${exercise.description}", style = MaterialTheme.typography.body2)
    }
}

data class Exercise(
    val name: String,
    val setsReps: String,
    val equipment: String,
    val description: String
)

data class WorkoutGoal(
    val name: String,
    var isActive: Boolean = false,
    val exercises: List<Exercise>

)


@Composable
fun NutritionSection() {
    val nutritionCategories = listOf(
        "Meal Plans",
        "Recipes",
        "Dietary Preferences"
    )
    var selectedCategory by remember { mutableStateOf(nutritionCategories.first()) }
    val nutritionOptions = mapOf(
        "Meal Plans" to listOf("Weight Loss", "Muscle Gain", "Maintenance"),
        "Recipes" to listOf("Breakfast", "Lunch", "Dinner", "Snacks"),
        "Dietary Preferences" to listOf("Vegetarian", "Vegan", "Low Carb", "Keto")
    )
    var selectedOption by remember { mutableStateOf(nutritionOptions[selectedCategory]?.first().orEmpty()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Select Your Nutrition Category:", style = MaterialTheme.typography.h6)
        DropdownMenuBox(nutritionCategories, selectedCategory) { category ->
            selectedCategory = category
            selectedOption = nutritionOptions[category]?.first().orEmpty()
        }

        nutritionOptions[selectedCategory]?.let { options ->
            options.forEach { option ->
                Row(modifier = Modifier.padding(8.dp)) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { selectedOption = option }
                    )
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DynamicContentDisplay(selectedCategory, selectedOption)
    }
}

@Composable
fun DropdownMenuBox(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedItem)
            Icon(Icons.Filled.ArrowDropDown, "dropdown")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
    }
}

@Composable
fun DynamicContentDisplay(category: String, option: String) {
    val contentDescription = when(category) {
        "Meal Plans" -> when(option) {
            "Weight Loss" -> "A plan focused on calorie deficit and nutrient-rich foods."
            "Muscle Gain" -> "High-protein meals and frequent eating schedule."
            "Maintenance" -> "Balanced meals to maintain your current body weight."
            else -> ""
        }
        "Recipes" -> when(option) {
            "Breakfast" -> "Healthy oatmeal pancakes recipe."
            "Lunch" -> "Quinoa salad with mixed vegetables."
            "Dinner" -> "Grilled salmon with asparagus."
            "Snacks" -> "Homemade granola bars."
            else -> ""
        }
        "Dietary Preferences" -> when(option) {
            "Vegetarian" -> "Plant-based meals excluding meat."
            "Vegan" -> "Strictly plants only, no animal products."
            "Low Carb" -> "Minimizing carbohydrate intake."
            "Keto" -> "High-fat, adequate-protein, low-carbohydrate diet."
            else -> ""
        }
        else -> "Please select a category and option."
    }

    Text(
        text = contentDescription,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 16.dp)
    )
}

data class WeightGoal(val currentWeight: Int, val goalWeight: Int)
data class PersonalRecord(val exerciseName: String, val weightLifted: Int)
data class WorkoutVolume(val date: String, val volume: Int)

val sampleWeightGoal = WeightGoal(currentWeight = 150, goalWeight = 140)
val samplePRs = listOf(
    PersonalRecord("Deadlift", 250),
    PersonalRecord("Squat", 200),
    PersonalRecord("Bench Press", 150)
)
val sampleWorkoutVolume = listOf(
    WorkoutVolume("Jan", 10000),
    WorkoutVolume("Feb", 12000),
    WorkoutVolume("Mar", 15000),
    WorkoutVolume("Apr", 18000)
)
const val sampleConsecutiveDays = 4 // Simulating 4 consecutive days of workouts

@Composable
fun ProgressSection() {
    val progress = remember { mutableFloatStateOf(0.75f) } // 75% towards the goal

    Column(modifier = Modifier.padding(16.dp)) {
        AchievementBox("Weight Goal", Icons.Default.Scale) {
            WeightGoalProgress(sampleWeightGoal, progress.floatValue)
        }
        AchievementBox("Personal Records", Icons.Default.FitnessCenter) {
            PersonalRecordsTable(samplePRs)
        }
        AchievementBox("Consecutive Days", Icons.Default.EventAvailable) {
            ConsecutiveDaysAchievement(sampleConsecutiveDays)
        }
        AchievementBox("Workout Volume", Icons.Default.Timeline) {
            WorkoutVolumeChart(sampleWorkoutVolume)
        }
    }
}

@Composable
fun AchievementBox(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
            content()
        }
    }
}

@Composable
fun WeightGoalProgress(weightGoal: WeightGoal, progress: Float) {
    Column {
        Text("Weight Goal: ${weightGoal.currentWeight}lbs / ${weightGoal.goalWeight}lbs")
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            backgroundColor = Color.LightGray
        )
    }
}

@Composable
fun PersonalRecordsTable(prs: List<PersonalRecord>) {
    Column {
        prs.forEach { pr ->
            Text("${pr.exerciseName}: ${pr.weightLifted}lbs")
        }
    }
}

@Composable
fun ConsecutiveDaysAchievement(days: Int) {
    Text("Consecutive Workout Days: $days")
}

@Composable
fun WorkoutVolumeChart(volumeData: List<WorkoutVolume>) {
    Column {
        volumeData.forEach { volume ->
            Text("${volume.date}: ${volume.volume} units")
        }
    }
}

@Composable
fun CommunitySection() {
    val communitySections = listOf(
        "Recipes" to Icons.Filled.Restaurant,
        "Achievements" to Icons.Filled.Star,
        "Food and Nutrition" to Icons.Filled.LocalCafe,
        "Goals" to Icons.Filled.Flag,
        "Success Stories" to Icons.Filled.EmojiEvents,
        "Motivation and Support" to Icons.Filled.Group,
        "News and Announcements" to Icons.AutoMirrored.Filled.Announcement
    )

    Column(modifier = Modifier.padding(16.dp)) {
        communitySections.forEach { (title, icon) ->
            SectionItem(title, icon)
        }
    }
}

@Composable
fun SectionItem(title: String, icon: ImageVector) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, modifier = Modifier.padding(end = 8.dp))
                Text(text = title, style = MaterialTheme.typography.h6)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }

            // Toggleable content
            if (expanded) {
                Text("Thanks for viewing $title. This is a work in progress!!, Configuring Realtime Database ")
            }
        }
    }
}

// Manage routing
sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    data object Workout : Screen("workout", Icons.Filled.FitnessCenter, "Workout")
    data object Nutrition : Screen("nutrition", Icons.Filled.LocalDining, "Nutrition")
    data object Progress : Screen("progress", Icons.AutoMirrored.Filled.ShowChart, "Progress")
    data object Community : Screen("community", Icons.Filled.People, "Community")
}