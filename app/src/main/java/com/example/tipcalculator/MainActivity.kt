@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tipcalculator

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipTimeLayoutPreview() {
    TipCalculatorTheme {
        TipTimeLayout()
    }
}

@Composable
@VisibleForTesting
fun TipTimeLayout(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("")}
    val amount = amountInput.toDoubleOrNull()?:0.0
    var tipInput by remember { mutableStateOf("") }
    val tipPercent = tipInput.toDoubleOrNull()?:0.0
//Swither
    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(amount, tipPercent, roundUp)
    Box(
        modifier = Modifier
            .paint(painterResource(id = R.drawable.menu2),
                contentScale = ContentScale.FillHeight
            )
    ){

    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            value = amountInput,
            onValueChange = {amountInput = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        EditNumberField(
            label =R.string.how_was_the_service ,
            leadingIcon = R.drawable.percent,
            value = tipInput,
            onValueChange ={tipInput = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged ={roundUp=it},
            modifier = Modifier.padding(bottom = 28.dp)
        )
        Spacer(modifier = Modifier.height(150.dp))
    }

}
@VisibleForTesting
internal fun calculateTip(amount:Double, tipPercent:Double = 20.0, roundUp: Boolean):String{
    var tip = tipPercent / 100 * amount
    if (roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun EditNumberField(
    @StringRes label:Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChange: (String)-> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier=Modifier
){
    val myColor = Color(218, 165, 32)
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = myColor,
            focusedIndicatorColor = myColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun RoundTheTipRow(
    roundUp:Boolean,
    onRoundUpChanged: (Boolean)->Unit,
    modifier: Modifier = Modifier)
{
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = stringResource(id = R.string.round_up_tip),
        )

        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier= Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}


