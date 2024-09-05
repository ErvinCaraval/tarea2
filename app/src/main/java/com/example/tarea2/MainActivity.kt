package com.example.tarea2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tarea2.ui.theme.Tarea2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tarea2Theme {
                CalculaNotaScreen()
            }
        }
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculaNotaScreen() {
    var nombreEstudiante by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }
    var nota1 by remember { mutableStateOf("") }
    var nota2 by remember { mutableStateOf("") }
    var conPenalizacion by remember { mutableStateOf(false) }
    var notaFinal by remember { mutableStateOf<Float?>(null) }
    var resultado by remember { mutableStateOf("") }

    val context = LocalContext.current  // Correcta referencia al contexto

    val botonHabilitado = nombreEstudiante.isNotEmpty() && curso.isNotEmpty() && nota1.isNotEmpty() && nota2.isNotEmpty()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            // Campo de nombre del estudiante
            TextField(
                value = nombreEstudiante,
                onValueChange = { nombreEstudiante = it },
                label = { Text("Nombre estudiante") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo del nombre del curso
            TextField(
                value = curso,
                onValueChange = { curso = it },
                label = { Text("Nombre curso") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de nota 1
            TextField(
                value = nota1,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) nota1 = it },
                label = { Text("Digita nota 1") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de nota 2
            TextField(
                value = nota2,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) nota2 = it },
                label = { Text("Digita nota 2") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Checkbox para penalización
            Row {
                Checkbox(
                    checked = conPenalizacion,
                    onCheckedChange = { conPenalizacion = it }
                )
                Text(text = "¿El curso ha sido realizado más de una vez?")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para calcular la nota
            Button(
                onClick = {
                    val estudiante = Estudiante(
                        nombreEstudiante = nombreEstudiante,
                        curso = curso,
                        nota1 = nota1.toFloat(),
                        nota2 = nota2.toFloat()
                    )
                    notaFinal = estudiante.calcularNota(conPenalizacion)
                    resultado = estudiante.resultadoFinal()

                    // Mostrar resultado en un mensaje emergente (Toast)
                    Toast.makeText(
                        context,  // Uso de `context` en lugar de `LocalContext.current`
                        "Resultado: $resultado",
                        Toast.LENGTH_LONG
                    ).show()

                    // Limpiar campos
                    nombreEstudiante = ""
                    curso = ""
                    nota1 = ""
                    nota2 = ""
                    conPenalizacion = false
                },
                enabled = botonHabilitado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (botonHabilitado) Color(0xFF6200EE) else Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular Nota")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el resultado si se ha calculado la nota final
            notaFinal?.let {
                Text("Nombre: $nombreEstudiante")
                Text("Curso: $curso")
                Text("Nota1: $nota1")
                Text("Nota2: $nota2")
                Text("Nota Final: $notaFinal")
            }
        }
    }
}

// Clase Estudiante
 class Estudiante(
    val nombreEstudiante: String,
    val curso: String,
    val nota1: Float,
    val nota2: Float
) {
    // Función para calcular la nota final
    fun calcularNota(conPenalizacion: Boolean): Float {
        var notaFinal = (nota1 + nota2) / 2
        if (conPenalizacion) {
            notaFinal -= 0.5f
        }
        return notaFinal.coerceIn(1.0f, 5.0f) // Asegura que la nota esté entre 1 y 5
    }

    // Función para determinar si aprobó o reprobó
    fun resultadoFinal(): String {
        val notaFinal = calcularNota(false)
        return if (notaFinal >= 3) {
            "Aprobado"
        } else {
            "Reprobado"
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Tarea2Theme {
        CalculaNotaScreen()
    }
}

